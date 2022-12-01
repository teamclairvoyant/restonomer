package com.clairvoyant.restonomer.core.app

import akka.actor.ActorSystem
import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.akkahttp.AkkaHttpBackend
import sttp.client3.{Response, SttpBackend, UriContext}
import sttp.model.HeaderNames._
import sttp.model.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}
import scala.concurrent.{Await, Future}
import scala.util.Random

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  private val maxRetries = 20

  private val statusToRetry: Set[StatusCode] = Set(
    StatusCode.Forbidden,
    StatusCode.TooManyRequests,
    StatusCode.BadGateway,
    StatusCode.Unauthorized,
    StatusCode.InternalServerError,
    StatusCode.GatewayTimeout,
    StatusCode.ServiceUnavailable,
    StatusCode.RequestTimeout
  )

  val r: Random.type = scala.util.Random
  private def sleepTimeInSeconds: Int = 10 + r.nextInt(20 - 10) + 1

  def run(checkpointConfig: CheckpointConfig): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem(checkpointConfig.name)
    implicit val akkaHttpBackend: SttpBackend[Future, Any] = AkkaHttpBackend.usingActorSystem(actorSystem)

    val restonomerRequest =
      RestonomerRequest
        .builder(checkpointConfig.request)
        .build

    val restonomerResponseData = getRestonomerResponseData(restonomerRequest)

    val restonomerResponseDF = restonomerResponseData.map {
      ResponseToDataFrameConverter(checkpointConfig.response.body.format).convertResponseToDataFrame
    }

    val restonomerResponseTransformedDF = restonomerResponseDF.map { df =>
      checkpointConfig.response.transformations
        .foldLeft(df) { case (df, restonomerTransformation) =>
          restonomerTransformation.transform(df)
        }
    }
    restonomerResponseTransformedDF.map(_.show())

    val persistedRestonomerResponseDF = persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.response.persistence
    )

    Await.result(persistedRestonomerResponseDF, Duration.Inf)
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

  private def getRestonomerResponseData(
      restonomerRequest: RestonomerRequest,
      retries: Int = 0
  )(implicit actorSystem: ActorSystem, akkaHttpBackend: SttpBackend[Future, Any]): Future[String] = {
    restonomerRequest
      .send(akkaHttpBackend)
      .httpResponse
      .flatMap {
        case response @ Response(_, StatusCode.Ok, _, _, _, _) if retries < maxRetries =>
          response.body match {
            case Left(_) =>
              getRestonomerResponseData(restonomerRequest, retries + 1)
            case Right(responseBody) =>
              Future(responseBody)
          }

        case Response(_, StatusCode.Found, _, headers, _, requestMetadata) =>
          getRestonomerResponseData(
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
            whatToRetry = getRestonomerResponseData(restonomerRequest, retries + 1)
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

  private def persistRestonomerResponseDataFrame(
      restonomerResponseDF: Future[DataFrame],
      restonomerPersistence: RestonomerPersistence
  ): Future[Unit] = {
    val dataFrameWriter =
      restonomerPersistence match {
        case FileSystem(fileFormat, filePath) =>
          new DataFrameToFileSystemWriter(
            sparkSession = sparkSession,
            fileFormat = fileFormat,
            filePath = filePath
          )
      }

    restonomerResponseDF.map(restonomerPersistence.persist(_, dataFrameWriter))
  }

}

private object RestonomerWorkflow {

  def apply(applicationConfig: ApplicationConfig): RestonomerWorkflow = {
    implicit val sparkSession: SparkSession = SparkSession
      .builder()
      .config(
        applicationConfig.sparkConfigs
          .map { sparkConfigs =>
            sparkConfigs.foldLeft(new SparkConf()) { case (sparkConf, sparkConfig) =>
              sparkConf.set(sparkConfig._1, sparkConfig._2)
            }
          }
          .getOrElse(new SparkConf())
      )
      .getOrCreate()

    new RestonomerWorkflow()
  }

}
