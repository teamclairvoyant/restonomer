package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.TokenConfig
import com.jayway.jsonpath.JsonPath
import sttp.client3.SttpBackend

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TokenRequestHandler {

  def getToken(
      tokenConfig: TokenConfig
  )(implicit akkaHttpBackend: SttpBackend[Future, Any]): String => String = {
    val tokenHttpResponse = Await.result(
      RestonomerRequest
        .builder(tokenConfig.tokenRequest)
        .build
        .httpRequest
        .send(akkaHttpBackend),
      Duration.Inf
    )

    TokenResponsePlaceholders(tokenConfig.tokenResponse.placeholder) match {
      case RESPONSE_BODY =>
        tokenJsonPath =>
          JsonPath.read[String](
            tokenHttpResponse.body match {
              case Left(errorMessage) =>
                throw new RestonomerException(errorMessage)
              case Right(responseBody) =>
                responseBody
            },
            s"$$.$tokenJsonPath"
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
  }

}
