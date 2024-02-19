package com.clairvoyant.restonomer.token

import cats.syntax.eq.*
import com.clairvoyant.restonomer.*
import com.clairvoyant.restonomer.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.common.TokenResponsePlaceholders.{ResponseBody, ResponseHeaders}
import com.clairvoyant.restonomer.exception.RestonomerException
import com.clairvoyant.restonomer.http.RestonomerRequest
import com.clairvoyant.restonomer.model.{RequestConfig, TokenConfig}
import com.jayway.jsonpath.JsonPath
import sttp.client3.*

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TokenHelper {

  private def buildTokenRequest(tokenRequestConfig: RequestConfig) =
    RestonomerRequest
      .builder[String](
        requestConfig = tokenRequestConfig,
        httpResponseType = asString
      )
      .build

  private def getTokenHttpResponse(tokenRequest: RequestConfig): HttpResponse[String] =
    Await.result(
      buildTokenRequest(tokenRequest).httpRequest.send(sttpBackend),
      Duration.Inf
    )

  def tokenFunction(token: Option[TokenConfig]): Option[String => String] =
    token.map { tokenConfig =>
      val tokenHttpResponse = getTokenHttpResponse(tokenConfig.tokenRequest)

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

}
