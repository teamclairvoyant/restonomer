package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders.{RESPONSE_BODY, RESPONSE_HEADERS}
import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.{RestonomerRequest, RestonomerResponse}
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import com.jayway.jsonpath.JsonPath
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.{HttpClientFutureBackend, Response, SttpBackend}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    implicit val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()

    val tokenFunction = checkpointConfig.token
      .map { tokenConfig =>
        getTokenFunction(
          tokenHttpResponse = Await.result(
            RestonomerRequest
              .builder(tokenConfig.tokenRequest)
              .build
              .httpRequest
              .send(sttpBackend),
            Duration.Inf
          ),
          tokenResponsePlaceholder = tokenConfig.tokenResponsePlaceholder
        )
      }

    val dataRestonomerRequest =
      RestonomerRequest
        .builder(checkpointConfig.data.dataRequest)(tokenFunction)
        .build

    val dataRestonomerResponse = RestonomerResponse.fetchFromRequest(
      restonomerRequest = dataRestonomerRequest,
      retryConfig = checkpointConfig.data.dataRequest.retry,
      restonomerPagination = checkpointConfig.data.dataResponse.pagination
    )

    val restonomerResponseDF = dataRestonomerResponse.body
      .map { httpResponseBody =>
        val responseDF = ResponseToDataFrameConverter(checkpointConfig.data.dataResponse.bodyFormat)
          .convertResponseToDataFrame(httpResponseBody.toSeq)

        checkpointConfig.data.dataResponse.dataColumn
          .map { dataColumn =>
            responseDF
              .select(explode(col(dataColumn)).as("records"))
              .select("records.*")
          }
          .getOrElse(responseDF)
      }

    val restonomerResponseTransformedDF = restonomerResponseDF.map { df =>
      checkpointConfig.data.dataResponse.transformations
        .foldLeft(df) { case (df, restonomerTransformation) =>
          restonomerTransformation.transform(df)
        }
    }

    val persistedRestonomerResponseDF = persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.data.dataResponse.persistence
    )

    Await.result(persistedRestonomerResponseDF, Duration.Inf)
  }

  private def getTokenFunction(
      tokenHttpResponse: Response[Either[String, String]],
      tokenResponsePlaceholder: String
  ): String => String =
    TokenResponsePlaceholders(tokenResponsePlaceholder) match {
      case RESPONSE_BODY =>
        tokenJsonPath =>
          JsonPath.read[String](
            tokenHttpResponse.body match {
              case Left(errorMessage) =>
                throw new RestonomerException(errorMessage)
              case Right(responseBody) =>
                responseBody
            },
            tokenJsonPath
          )

      case RESPONSE_HEADERS =>
        tokenName =>
          tokenHttpResponse.headers
            .find(_.name == tokenName) match {
            case Some(header) =>
              header.value
            case None =>
              throw new RestonomerException(s"Could not find the value of $tokenName in the token response.")
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
