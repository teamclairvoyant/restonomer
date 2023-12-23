package com.clairvoyant.restonomer.http

import cats.syntax.eq.*
import com.clairvoyant.restonomer.common.*
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes.*
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
      compression: Option[String],
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
          httpRequest = compression
            .map {
              ResponseBodyCompressionTypes(_) match
                case GZIP => httpRequest.response(asByteArray)
            }
            .getOrElse(httpRequest.response(asString)),
          statusCodesToRetry = retryConfig.statusCodesToRetry.map(StatusCode(_)),
          maxRetries = retryConfig.maxRetries
        )
      )
    }
  }

  private def sleepTimeInSeconds: Int = 10 + random.nextInt(10) + 1

  private def getBody[T](
      httpRequest: Request[Either[String, T], Any],
      statusCodesToRetry: List[StatusCode],
      maxRetries: Int,
      currentRetryAttemptNumber: Int = 0
  ): Future[Seq[String]] = {
    httpRequest
      .send(sttpBackend)
      .flatMap {
        case Response(body, StatusCode.Ok, _, _, _, _) =>
          body match {
            case Right(responseBody: String) => Future(Seq(responseBody))

            case Right(responseBody: Array[Byte]) =>
              val gzipStream = new GZIPInputStream(new ByteArrayInputStream(responseBody))
              val inputStreamReader = new InputStreamReader(gzipStream)
              val bufferedReader = new BufferedReader(inputStreamReader)
              Future(Iterator.continually(bufferedReader.readLine()).takeWhile(_ != null).toSeq)

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
