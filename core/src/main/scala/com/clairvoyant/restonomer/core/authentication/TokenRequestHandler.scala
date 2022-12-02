package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.TokenConfig
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods
import sttp.client3.SttpBackend

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TokenRequestHandler {

  def getTokensMap(
      tokenConfig: TokenConfig
  )(implicit akkaHttpBackend: SttpBackend[Future, Any]): Map[String, String] = {
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
        implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats

        JsonMethods
          .parse(
            tokenHttpResponse.body match {
              case Left(errorMessage) =>
                throw new RestonomerException(errorMessage)
              case Right(responseBody) =>
                responseBody
            }
          )
          .extract[Map[String, String]]

      case RESPONSE_HEADERS =>
        tokenHttpResponse.headers.map(header => header.name -> header.value).toMap
    }
  }

}
