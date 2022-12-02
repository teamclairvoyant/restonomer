package com.clairvoyant.restonomer.core.http

import akka.actor.ActorSystem
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.{Response, SttpBackend, UriContext}
import sttp.model.HeaderNames.Location
import sttp.model.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

case class RestonomerResponse(body: Future[String])

object RestonomerResponse {

  private val maxRetries = 20

  private val statusToRetry = Set(
    StatusCode.Forbidden,
    StatusCode.TooManyRequests,
    StatusCode.BadGateway,
    StatusCode.Unauthorized,
    StatusCode.InternalServerError,
    StatusCode.GatewayTimeout,
    StatusCode.ServiceUnavailable,
    StatusCode.RequestTimeout
  )

  private val random: Random.type = scala.util.Random

  def fetchFromRequest(restonomerRequest: RestonomerRequest)(
      implicit actorSystem: ActorSystem,
      akkaHttpBackend: SttpBackend[Future, Any]
  ): RestonomerResponse = RestonomerResponse(getBody(restonomerRequest))

  private def sleepTimeInSeconds: Int = 10 + random.nextInt(20 - 10) + 1

  private def getBody(restonomerRequest: RestonomerRequest, retries: Int = 0)(
      implicit actorSystem: ActorSystem,
      akkaHttpBackend: SttpBackend[Future, Any]
  ): Future[String] = {
    restonomerRequest.httpRequest
      .send(akkaHttpBackend)
      .flatMap {
        case Response(body, StatusCode.Ok, _, _, _, _) if retries < maxRetries =>
          body match {
            case Left(_) =>
              waitBeforeRetry(
                retryAfter = (sleepTimeInSeconds * 1000).millis,
                whatToRetry = getBody(restonomerRequest, retries + 1)
              )
            case Right(responseBody) =>
              Future(responseBody)
          }

        case Response(_, StatusCode.Found, _, headers, _, requestMetadata) =>
          getBody(
            restonomerRequest
              .copy(httpRequest =
                restonomerRequest.httpRequest
                  .method(
                    method = requestMetadata.method,
                    uri = uri"${headers.find(_.name == Location).get}"
                  )
              ),
            retries
          )

        case Response(_, statusCode, _, headers, _, _) if statusToRetry.contains(statusCode) && retries < maxRetries =>
          waitBeforeRetry(
            retryAfter =
              headers
                .find(_.name.toLowerCase == "retry-after")
                .map(_.value.toInt * 1000)
                .getOrElse(sleepTimeInSeconds * 1000)
                .millis,
            whatToRetry = getBody(restonomerRequest, retries + 1)
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
            s"Something totally unexpected bad happened while calling the API $retries times."
          )
      }

  }

  private def waitBeforeRetry[T](retryAfter: FiniteDuration, whatToRetry: => Future[T])(
      implicit actorSystem: ActorSystem
  ): Future[T] =
    akka.pattern.after(
      duration = retryAfter,
      using = actorSystem.scheduler
    )(whatToRetry)

}
