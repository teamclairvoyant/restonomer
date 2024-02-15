package com.clairvoyant.restonomer.http

import cats.syntax.eq.*
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes.*
import com.clairvoyant.restonomer.common.*
import com.clairvoyant.restonomer.exception.RestonomerException
import com.clairvoyant.restonomer.model.RetryConfig
import com.clairvoyant.restonomer.pagination.RestonomerPagination
import com.clairvoyant.restonomer.sttpBackend
import odelay.Delay
import sttp.client3.*
import sttp.model.HeaderNames.Location
import sttp.model.{Header, StatusCode}

import java.io.{BufferedReader, ByteArrayInputStream, InputStreamReader}
import java.util.zip.GZIPInputStream
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Random

case class RestonomerResponse(body: Future[Seq[String]])

object RestonomerResponse {

  private val random: Random.type = scala.util.Random

  def fetchFromRequest(
      httpRequest: Request[Either[String, String], Any],
      isCompressed: Boolean,
      isText: Boolean,
      retryConfig: RetryConfig,
      restonomerPagination: Option[RestonomerPagination]
  ): RestonomerResponse = {
    def getPages(httpResponseBody: Future[Seq[String]]): Future[Seq[String]] = {
      restonomerPagination
        .map { pagination =>
          httpResponseBody.flatMap { httpResponseBodySeq =>
            pagination
              .getNextPageToken(httpResponseBodySeq.last)
              .map { nextPageToken =>
                getPages(
                  httpResponseBody = getBody(
                    httpRequest = httpRequest.method(
                      method = httpRequest.method,
                      uri = pagination.placeNextTokenInURL(
                        uri = httpRequest.uri,
                        nextPageToken = nextPageToken
                      )
                    ),
                    isText = isText,
                    isCompressed = isCompressed,
                    statusCodesToRetry = retryConfig.statusCodesToRetry.map(StatusCode(_)),
                    maxRetries = retryConfig.maxRetries
                  ).map(httpResponseBodySeq ++ _)
                )
              }
              .getOrElse(httpResponseBody)
          }
        }
        .getOrElse(httpResponseBody)
    }

    RestonomerResponse {
      getPages(
        httpResponseBody = getBody(
          httpRequest = httpRequest,
          isText = isText,
          isCompressed = isCompressed,
          statusCodesToRetry = retryConfig.statusCodesToRetry.map(StatusCode(_)),
          maxRetries = retryConfig.maxRetries
        )
      )
    }
  }

  private def sleepTimeInSeconds: Int = 10 + random.nextInt(10) + 1

  private def getBody[T](
      httpRequest: Request[Either[String, T], Any],
      isText: Boolean,
      isCompressed: Boolean,
      statusCodesToRetry: List[StatusCode],
      maxRetries: Int,
      currentRetryAttemptNumber: Int = 0
  ): Future[Seq[T]] = {

    val response = if isText && !isCompressed then httpRequest.response(asString) else httpRequest.response(asByteArray)
    
    response.send(sttpBackend)
      .flatMap {
        case Response(body, StatusCode.Ok, _, _, _, _) =>
          body match {
            case Right(responseBody: T) => {
              responseBody match {
                case textResponse: String => Future(Seq(textResponse))
                case byteResponse: Array[Byte] =>
                  if(isText && isCompressed) {

                    Future(Seq(byteResponse))
                    // if response is compressed and text convert to string and return
//                    val gzipStream = new GZIPInputStream(new ByteArrayInputStream(byteResponse))
//                    val inputStreamReader = new InputStreamReader(gzipStream)
//                    val bufferedReader = new BufferedReader(inputStreamReader)
//                    Future(Iterator.continually(bufferedReader.readLine()).takeWhile(_ != null).toSeq)
                  } else {
                    // it is excel (or PDF) file, return as is (Array[String])
                    Future(Seq(byteResponse))
                  }
              }
            }
            case _ => Future(Seq.empty)
          }

        case response @ Response(_, statusCode, _, headers, _, _)
            if statusCodesToRetry.contains(statusCode) && currentRetryAttemptNumber < maxRetries =>
          waitBeforeRetry(
            whatToRetry = getBody(
              httpRequest = httpRequest
                .header(
                  k = "retry-attempt",
                  v = (currentRetryAttemptNumber + 1).toString,
                  replaceExisting = true
                ),
              isText = isText,
              isCompressed = isCompressed,
              statusCodesToRetry = statusCodesToRetry,
              maxRetries = maxRetries,
              currentRetryAttemptNumber = currentRetryAttemptNumber + 1
            ),
            message = response.toString(),
            maxRetries = maxRetries,
            currentRetryAttemptNumber = currentRetryAttemptNumber,
            headers = headers
          )

        case Response(_, StatusCode.Found, _, headers, _, requestMetadata) =>
          getBody(
            httpRequest = httpRequest.method(
              method = requestMetadata.method,
              uri = uri"${headers.find(_.name == Location).get}"
            ),
            isText = isText,
            isCompressed = isCompressed,
            statusCodesToRetry = statusCodesToRetry,
            maxRetries = maxRetries,
            currentRetryAttemptNumber = currentRetryAttemptNumber + 1
          )

        case Response(_, StatusCode.NoContent, _, _, _, _) => throw new RestonomerException("No Content.")

        case _ =>
          throw new RestonomerException(
            s"Something totally unexpected bad happened while calling the API ${currentRetryAttemptNumber + 1} times."
          )
      }
  }

  private def waitBeforeRetry[T](
      whatToRetry: => Future[T],
      message: String,
      maxRetries: Int,
      currentRetryAttemptNumber: Int,
      headers: Seq[Header] = Seq.empty
  ): Future[T] = {
    val retryAfterInSeconds =
      headers
        .find(_.name.toLowerCase === "retry-after")
        .map(_.value.toInt)
        .getOrElse(sleepTimeInSeconds)
        .seconds

    println(
      s"""
         |message: $message
         |currentRetryAttempt: $currentRetryAttemptNumber, 
         |maxRetries: $maxRetries, 
         |retryAfterInSeconds: $retryAfterInSeconds
         |""".stripMargin
    )

    Delay(retryAfterInSeconds)(whatToRetry).future.flatMap(identity)
  }

}
