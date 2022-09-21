package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

class RestonomerWorkflow(implicit val sparkSession: SparkSession) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest =
      RestonomerRequest
        .builder(checkpointConfig.request.method, checkpointConfig.request.url)
        .withAuthentication(checkpointConfig.request.authentication)
        .withHeaders(checkpointConfig.request.headers)
        .build

    val restonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    val restonomerResponseBody =
      restonomerResponse.httpResponse.body match {
        case Left(errorMessage) =>
          throw new RestonomerException(errorMessage)
        case Right(responseBody) =>
          responseBody
      }

    val restonomerResponseDataFrame = ResponseToDataFrameConverter(checkpointConfig.response.body.format)
      .convertResponseToDataFrame(restonomerResponseBody)

    restonomerResponseDataFrame.show(truncate = false)
  }

}

object RestonomerWorkflow {

  def apply(applicationConfig: ApplicationConfig): RestonomerWorkflow = {
    val sparkConf = new SparkConf()

    implicit val sparkSession: SparkSession = SparkSession
      .builder()
      .config(
        applicationConfig.sparkConfigs
          .map { sparkConfigs =>
            sparkConfigs.foldLeft(sparkConf) { case (sparkConf, sparkConfig) =>
              sparkConf.set(sparkConfig._1, sparkConfig._2)
            }
          }
          .getOrElse(sparkConf)
      )
      .getOrCreate()

    new RestonomerWorkflow()
  }

}
