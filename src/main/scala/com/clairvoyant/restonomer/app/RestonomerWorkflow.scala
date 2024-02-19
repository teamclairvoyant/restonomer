package com.clairvoyant.restonomer.app

import com.clairvoyant.restonomer.*
import com.clairvoyant.restonomer.body.{Excel, Text}
import com.clairvoyant.restonomer.http.*
import com.clairvoyant.restonomer.model.*
import com.clairvoyant.restonomer.token.TokenHelper.*
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.*

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig)(using sparkSession: SparkSession): Unit = {
    checkpointConfig.data.dataResponse.body match {
      case Text(_, None)    => start[String](checkpointConfig, asString)
      case Text(_, Some(_)) => start[Array[Byte]](checkpointConfig, asByteArray)
      case Excel(_, _)      => start[Array[Byte]](checkpointConfig, asByteArray)
    }
  }

  private def start[T <: String | Array[Byte]](checkpointConfig: CheckpointConfig, httpResponseType: HttpResponseAs[T])(
      using sparkSession: SparkSession
  ): Unit = {
    val restonomerRequest =
      RestonomerRequest
        .builder[T](
          requestConfig = checkpointConfig.data.dataRequest,
          httpResponseType = httpResponseType
        )(using tokenFunction(checkpointConfig.token))
        .build

    val dataRestonomerResponse = RestonomerResponse.fetchFromRequest[T](
      httpRequest = restonomerRequest.httpRequest,
      retryConfig = checkpointConfig.data.dataRequest.retry,
      restonomerPagination = checkpointConfig.data.dataResponse.pagination
    )

    val restonomerResponseDF = dataRestonomerResponse.body
      .map(checkpointConfig.data.dataResponse.body.read[T])

    val restonomerResponseTransformedDF = restonomerResponseDF.map { df =>
      checkpointConfig.data.dataResponse.transformations
        .foldLeft(df) { case (df, restonomerTransformation) => restonomerTransformation.transform(df) }
    }

    val persistedRestonomerResponseDF = restonomerResponseTransformedDF.map(
      checkpointConfig.data.dataResponse.persistence.persist
    )

    Await.result(persistedRestonomerResponseDF, Duration.Inf)
  }

}
