package com.clairvoyant.restonomer.app

import cats.syntax.eq.*
import com.clairvoyant.restonomer.body.{Excel, Text}
import com.clairvoyant.restonomer.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.common.TokenResponsePlaceholders.*
import com.clairvoyant.restonomer.exception.RestonomerException
import com.clairvoyant.restonomer.http.*
import com.clairvoyant.restonomer.model.*
import com.clairvoyant.restonomer.sttpBackend
import com.jayway.jsonpath.JsonPath
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.{ResponseAs, asByteArray, asString}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig)(using sparkSession: SparkSession): Unit = {
    checkpointConfig.data.dataResponse.body match
      case Text(_, Some(_)) => start[Array[Byte]](asByteArray)
      case Text(_, None)    => start[String](asString)
      case Excel(_, _)      => start[Array[Byte]](asByteArray)

    def start[T](httpResponseType: ResponseAs[Either[String, T], Any]): Unit = {
      val tokenFunction = checkpointConfig.token
        .map { tokenConfig =>
          val tokenHttpResponse = Await.result(
            RestonomerRequest
              .builder[String](
                requestConfig = tokenConfig.tokenRequest,
                httpResponseType = asString
              )
              .build
              .httpRequest
              .send(sttpBackend),
            Duration.Inf
          )

          TokenResponsePlaceholders(tokenConfig.tokenResponsePlaceholder) match {
            case ResponseBody =>
              (tokenJsonPath: String) =>
                JsonPath.read[String](
                  tokenHttpResponse.body match {
                    case Left(errorMessage)  => throw new RestonomerException(errorMessage)
                    case Right(responseBody) => responseBody
                  },
                  tokenJsonPath
                )

            case ResponseHeaders =>
              (tokenName: String) =>
                tokenHttpResponse.headers
                  .find(_.name === tokenName) match {
                  case Some(header) => header.value
                  case None =>
                    throw new RestonomerException(s"Could not find the value of $tokenName in the token response.")
                }
          }
        }

      val dataRestonomerRequest =
        RestonomerRequest
          .builder[T](
            requestConfig = checkpointConfig.data.dataRequest,
            httpResponseType = httpResponseType
          )(using tokenFunction)
          .build

      val dataRestonomerResponse = RestonomerResponse.fetchFromRequest[T](
        httpRequest = dataRestonomerRequest.httpRequest,
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

}
