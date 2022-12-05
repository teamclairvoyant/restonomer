package com.clairvoyant.restonomer.core.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.EntityStreamException
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.RetryConfig
import sttp.client3._
import sttp.model.HeaderNames.Location
import sttp.model.{Header, StatusCode}

import scala.collection.Seq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Random

case class RestonomerResponse(body: Future[String])

object RestonomerResponse {

  private val random: Random.type = scala.util.Random

  def fetchFromRequest(restonomerRequest: RestonomerRequest, retryConfig: RetryConfig)(
      implicit actorSystem: ActorSystem,
      akkaHttpBackend: SttpBackend[Future, Any]
  ): RestonomerResponse = {
    val httpResponseBody = getBody(
      restonomerRequest = restonomerRequest,
      statusCodesToRetry = retryConfig.statusCodesToRetry.map(StatusCode(_)),
      maxRetries = retryConfig.maxRetries
    )

    RestonomerResponse(body = httpResponseBody)
  }

  private def sleepTimeInSeconds: Int = 10 + random.nextInt(20 - 10) + 1

  private def getBody(
      restonomerRequest: RestonomerRequest,
      statusCodesToRetry: List[StatusCode],
      maxRetries: Int,
      currentRetryAttemptNumber: Int = 0
  )(implicit actorSystem: ActorSystem, akkaHttpBackend: SttpBackend[Future, Any]): Future[String] = {
    restonomerRequest.httpRequest
      .send(akkaHttpBackend)
      .flatMap {
        case Response(body, StatusCode.Ok, _, _, _, _) =>
          Future(body.toSeq.head)

        case Response(_, StatusCode.Found, _, headers, _, requestMetadata) =>
          val newUri = uri"${headers.find(_.name == Location).get}"
          println(s"##### Redirecting to $newUri #####")

          getBody(
            restonomerRequest = restonomerRequest
              .copy(httpRequest =
                restonomerRequest.httpRequest.method(
                  method = requestMetadata.method,
                  uri = newUri
                )
              ),
            statusCodesToRetry = statusCodesToRetry,
            maxRetries = maxRetries,
            currentRetryAttemptNumber = currentRetryAttemptNumber + 1
          )

        case response @ Response(_, statusCode, _, headers, _, _)
            if statusCodesToRetry.contains(statusCode) && currentRetryAttemptNumber < maxRetries =>
          waitBeforeRetry(
            whatToRetry = getBody(
              restonomerRequest = restonomerRequest,
              statusCodesToRetry = statusCodesToRetry,
              maxRetries = maxRetries,
              currentRetryAttemptNumber = currentRetryAttemptNumber + 1
            ),
            message = response.toString(),
            maxRetries = maxRetries,
            currentRetryAttemptNumber = currentRetryAttemptNumber,
            headers = headers
          )

        case Response(body, StatusCode.MovedPermanently, _, _, _, _) =>
          body match {
            case Left(errorMessage) =>
              throw new RestonomerException(errorMessage)
            case Right(responseBody) =>
              throw new RestonomerException(responseBody)
          }

        case Response(_, StatusCode.NoContent, _, _, _, _) =>
          throw new RestonomerException("No Content")

        case _ =>
          throw new RestonomerException(
            s"Something totally unexpected bad happened while calling the API $currentRetryAttemptNumber times."
          )
      }
      .recoverWith {
        case ex: EntityStreamException if currentRetryAttemptNumber < maxRetries =>
          waitBeforeRetry(
            whatToRetry = getBody(
              restonomerRequest = restonomerRequest,
              statusCodesToRetry = statusCodesToRetry,
              maxRetries = maxRetries,
              currentRetryAttemptNumber = currentRetryAttemptNumber + 1
            ),
            message = ex.getMessage,
            maxRetries = maxRetries,
            currentRetryAttemptNumber = currentRetryAttemptNumber
          )
      }

  }

  private def waitBeforeRetry[T](
      whatToRetry: => Future[T],
      message: String,
      maxRetries: Int,
      currentRetryAttemptNumber: Int,
      headers: Seq[Header] = Seq.empty
  )(implicit actorSystem: ActorSystem): Future[T] = {
    val retryAfterInSeconds =
      headers
        .find(_.name.toLowerCase == "retry-after")
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

    akka.pattern.after(
      duration = retryAfterInSeconds,
      using = actorSystem.scheduler
    )(whatToRetry)

  }

}
