package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import retry.Success
import sttp.client3.{Response, UriContext}
import sttp.model.HeaderNames._
import sttp.model.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.util.Random

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  private val maxRetries = 20

  val r: Random.type = scala.util.Random
  private def sleepTimeInSeconds: Int = 10 + r.nextInt(20 - 10) + 1

  private val statusToRetry: Set[StatusCode] = Set(
    StatusCode.Forbidden,
    StatusCode.TooManyRequests,
    StatusCode.BadGateway,
    StatusCode.Unauthorized,
    StatusCode.InternalServerError,
    StatusCode.GatewayTimeout,
    StatusCode.ServiceUnavailable,
    StatusCode.RequestTimeout,
    StatusCode(524)
  )

  def run(checkpointConfig: CheckpointConfig): Unit = {
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

    val persistedRestonomerResponseDF = persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.response.persistence
    )

    Await.result(persistedRestonomerResponseDF, Duration.Inf)

  }

  private def getRestonomerResponseData(
      restonomerRequest: RestonomerRequest,
      retries: Int = 0
  ): Future[String] = {
    restonomerRequest
      .send()
      .httpResponse
      .flatMap {
        case response @ Response(_, StatusCode.Ok, _, _, _, _) if retries <= maxRetries =>
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
              )
          )

        case Response(_, statusCode, _, headers, _, _) if statusToRetry.contains(statusCode) && retries < maxRetries =>
          val retryAfter = headers
            .find(_.name.toLowerCase == "retry-after")
            .map(_.value.toInt * 1000)
            .getOrElse(sleepTimeInSeconds * 1000)

          implicit val responseDataShouldNotBeBlank: Success[String] = Success[String](!_.isBlank)
          retry.Pause(1, retryAfter.millis).apply { () => getRestonomerResponseData(restonomerRequest, retries + 1) }
      }
  }

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
