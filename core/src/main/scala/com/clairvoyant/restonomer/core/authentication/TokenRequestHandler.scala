package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.TokenConfig
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods
import sttp.client3.SttpBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TokenRequestHandler {

  def getTokensMap(
      tokenConfig: TokenConfig
  )(implicit akkaHttpBackend: SttpBackend[Future, Any]): Map[String, String] = {
    @volatile var tokensMap = Map[String, String]()

    val tokenHttpResponse =
      RestonomerRequest
        .builder(tokenConfig.tokenRequest)
        .build
        .send(akkaHttpBackend)
        .httpResponse

    val tokensMapFuture = tokenHttpResponse.map { tokenResponse =>
      TokenResponsePlaceholders(tokenConfig.tokenResponse.placeholder) match {
        case RESPONSE_BODY =>
          implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats

          JsonMethods
            .parse(
              tokenResponse.body match {
                case Left(errorMessage) =>
                  throw new RestonomerException(errorMessage)
                case Right(responseBody) =>
                  responseBody
              }
            )
            .extract[Map[String, String]]

        case RESPONSE_HEADERS =>
          tokenResponse.headers.map(header => header.name -> header.value).toMap
      }
    }

    tokensMapFuture.onComplete {
      case Success(value) =>
        tokensMap = value
      case Failure(exception) =>
        exception.printStackTrace()
    }

    tokensMap
  }

  def substituteCredentialFromTokens(
      credential: String
  )(implicit tokens: Map[String, String]): String =
    """token\[(.*)]""".r
      .findFirstMatchIn(credential)
      .map { matcher =>
        tokens.get(matcher.group(1)) match {
          case Some(value) =>
            value
          case None =>
            throw new RestonomerException(
              s"Could not find the value of $credential in the token response: $tokens"
            )
        }
      }
      .getOrElse(credential)

}
