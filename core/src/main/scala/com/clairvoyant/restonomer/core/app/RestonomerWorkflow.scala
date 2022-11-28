package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.{RestonomerRequest, RestonomerResponse}
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.Response
import sttp.model.StatusCode

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  private val maxRetries = 20

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest =
      RestonomerRequest
        .builder(checkpointConfig.request)
        .build

    val restonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    val restonomerResponseBody = getRestonomerResponseBody(restonomerResponse)

    val restonomerResponseDF = ResponseToDataFrameConverter(checkpointConfig.response.body.format)
      .convertResponseToDataFrame(restonomerResponseBody)

    val restonomerResponseTransformedDF =
      checkpointConfig.response.transformations.foldLeft(restonomerResponseDF) {
        case (restonomerResponseDF, restonomerTransformation) =>
          restonomerTransformation.transform(restonomerResponseDF)
      }

    persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.response.persistence
    )
  }

  private def getRestonomerResponseBody(restonomerResponse: RestonomerResponse, retries: Int = 0): String = {
    restonomerResponse.httpResponse match {
      case response @ Response(_, StatusCode.Ok, _, _, _, _) if retries <= maxRetries =>
        response.body match {
          case Left(errorMessage) =>
            throw new RestonomerException(errorMessage)
          case Right(responseBody) =>
            responseBody
        }
    }
  }

  private def persistRestonomerResponseDataFrame(
      restonomerResponseDF: DataFrame,
      restonomerPersistence: RestonomerPersistence
  ): Unit = {
    val dataFrameWriter =
      restonomerPersistence match {
        case FileSystem(fileFormat, filePath) =>
          new DataFrameToFileSystemWriter(
            sparkSession = sparkSession,
            fileFormat = fileFormat,
            filePath = filePath
          )
      }

    restonomerPersistence.persist(restonomerResponseDF, dataFrameWriter)
  }

}

object RestonomerWorkflow {

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
