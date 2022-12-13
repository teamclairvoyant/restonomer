package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.{RestonomerRequest, RestonomerResponse}
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig, RequestConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.core.transformation.RestonomerTransformation
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.SttpBackend


class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
/*  val restonomerRequest = buildRestonomerRequest(checkpointConfig.request) */
    val restonomerRequest = RestonomerRequest.builder(checkpointConfig.request).build

/*    val restonomerResponse = getRestonomerResponse(
      restonomerRequest = restonomerRequest,
      httpBackendType = checkpointConfig.httpBackendType
    )

    val restonomerResponse = RestonomerResponse.fetchFromRequest(
      restonomerRequest = restonomerRequest,
      retryConfig = checkpointConfig.response.retry
    )

   val restonomerResponseBody = getRestonomerResponseBody(restonomerResponse)

    val restonomerResponseDF = restonomerResponse.body
      .map(ResponseToDataFrameConverter(checkpointConfig.response.body.format).convertResponseToDataFrame)

    val restonomerResponseTransformedDF = transformRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseDF,
      restonomerTransformations = checkpointConfig.response.transformations
    )

    persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.response.persistence
    )
  }

  private def buildRestonomerRequest(requestConfig: RequestConfig): RestonomerRequest =
    RestonomerRequest
      .builder(requestConfig)
      .withBody(requestConfig.body)
      .build

  private def getRestonomerResponse(
      restonomerRequest: RestonomerRequest,
      httpBackendType: String
  ): RestonomerResponse = restonomerRequest.send(httpBackendType)

  private def getRestonomerResponseBody(restonomerResponse: RestonomerResponse): String =
    restonomerResponse.httpResponse.body match {
      case Left(errorMessage) =>
        throw new RestonomerException(errorMessage)
      case Right(responseBody) =>
        responseBody
    }
*/
  def convertRestonomerResponseBodyToDataFrame(
      restonomerResponseBody: String,
      restonomerResponseBodyFormat: String
  ): DataFrame =
    ResponseToDataFrameConverter(restonomerResponseBodyFormat).convertResponseToDataFrame(restonomerResponseBody)

  def transformRestonomerResponseDataFrame(
      restonomerResponseDF: DataFrame,
      restonomerTransformations: List[RestonomerTransformation]
  ): DataFrame =
    restonomerTransformations.foldLeft(restonomerResponseDF) { case (restonomerResponseDF, restonomerTransformation) =>
      restonomerTransformation.transform(restonomerResponseDF)
    }

  def persistRestonomerResponseDataFrame(
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