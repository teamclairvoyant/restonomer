package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders.*
import com.clairvoyant.restonomer.core.converter.*
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.*
import com.clairvoyant.restonomer.core.model.*
import com.clairvoyant.restonomer.core.persistence.*
import com.clairvoyant.restonomer.core.sttpBackend
import com.jayway.jsonpath.JsonPath
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class RestonomerWorkflow(using sparkSession: SparkSession) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
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
        .builder(checkpointConfig.data.dataRequest)(using tokenFunction)
        .build

    val dataRestonomerResponse = RestonomerResponse.fetchFromRequest(
      restonomerRequest = dataRestonomerRequest,
      retryConfig = checkpointConfig.data.dataRequest.retry,
      restonomerPagination = checkpointConfig.data.dataResponse.pagination
    )

    val restonomerResponseDF = dataRestonomerResponse.body
      .map { httpResponseBody =>
        (checkpointConfig.data.dataResponse.body match {
          case JSON(
                columnNameOfCorruptRecord,
                dataColumnName,
                dateFormat,
                inferSchema,
                locale,
                multiLine,
                originalSchema,
                primitivesAsString,
                timestampFormat,
                timestampNTZFormat
              ) =>
            JSONResponseToDataFrameConverter(
              columnNameOfCorruptRecord,
              dataColumnName,
              dateFormat,
              inferSchema,
              locale,
              multiLine,
              originalSchema,
              primitivesAsString,
              timestampFormat,
              timestampNTZFormat
            )
          case CSV(
                columnNameOfCorruptRecord,
                dateFormat,
                emptyValue,
                enforceSchema,
                escape,
                header,
                inferSchema,
                ignoreLeadingWhiteSpace,
                ignoreTrailingWhiteSpace,
                lineSep,
                locale,
                multiLine,
                nanValue,
                nullValue,
                originalSchema,
                quote,
                recordSep,
                sep,
                timestampFormat,
                timestampNTZFormat
              ) =>
            CSVResponseToDataFrameConverter(
              columnNameOfCorruptRecord,
              dateFormat,
              emptyValue,
              enforceSchema,
              escape,
              header,
              inferSchema,
              ignoreLeadingWhiteSpace,
              ignoreTrailingWhiteSpace,
              lineSep,
              locale,
              multiLine,
              nanValue,
              nullValue,
              originalSchema,
              quote,
              recordSep,
              sep,
              timestampFormat,
              timestampNTZFormat
            )
        }).convertResponseToDataFrame(httpResponseBody.toSeq)
      }

    val restonomerResponseTransformedDF = restonomerResponseDF.map { df =>
      checkpointConfig.data.dataResponse.transformations
        .foldLeft(df) { case (df, restonomerTransformation) => restonomerTransformation.transform(df) }
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
      case ResponseBody =>
        tokenJsonPath =>
          JsonPath.read[String](
            tokenHttpResponse.body match {
              case Left(errorMessage)  => throw new RestonomerException(errorMessage)
              case Right(responseBody) => responseBody
            },
            tokenJsonPath
          )

      case ResponseHeaders =>
        tokenName =>
          tokenHttpResponse.headers
            .find(_.name == tokenName) match {
            case Some(header) => header.value
            case None => throw new RestonomerException(s"Could not find the value of $tokenName in the token response.")
          }
    }

  private def persistRestonomerResponseDataFrame(
      restonomerResponseDF: Future[DataFrame],
      restonomerPersistence: RestonomerPersistence
  ): Future[Unit] = restonomerResponseDF.map(restonomerPersistence.persist)

}

private object RestonomerWorkflow {

  def apply(applicationConfig: ApplicationConfig): RestonomerWorkflow = {
    given sparkSession: SparkSession =
      SparkSession
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
