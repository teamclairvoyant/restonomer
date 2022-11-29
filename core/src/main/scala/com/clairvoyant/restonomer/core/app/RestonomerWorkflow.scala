package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.{HttpClientFutureBackend, Request, Response, UriContext}
import sttp.model.HeaderNames._
import sttp.model.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  private val maxRetries = 20

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val httpRequest =
      RestonomerRequest
        .builder(checkpointConfig.request)
        .build
        .httpRequest

    val restonomerResponseData = getRestonomerResponseData(httpRequest)

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
      httpRequest: Request[Either[String, String], Any],
      retries: Int = 0
  ): Future[String] = {
    httpRequest
      .send(HttpClientFutureBackend())
      .flatMap {
        case response @ Response(_, StatusCode.Ok, _, _, _, _) if retries <= maxRetries =>
          response.body match {
            case Left(_) =>
              getRestonomerResponseData(httpRequest, retries + 1)
            case Right(responseBody) =>
              Future(responseBody)
          }

        case response @ Response(_, StatusCode.Found, _, _, _, _) =>
          getRestonomerResponseData(
            httpRequest.method(
              method = httpRequest.method,
              uri = uri"${response.header(Location).get}"
            )
          )
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
