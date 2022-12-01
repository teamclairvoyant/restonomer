package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication._
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders
import com.clairvoyant.restonomer.core.common.TokenResponsePlaceholders.{RESPONSE_BODY, RESPONSE_HEADERS}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.TokenConfig
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods
import sttp.client3.{Request, SttpBackend}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  private def getTokensMap(tokenConfig: TokenConfig, akkaHttpBackend: SttpBackend[Future, Any]): Map[String, String] = {
    val tokenHttpResponse = Await.result(
      RestonomerRequest
        .builder(tokenConfig.tokenRequest, akkaHttpBackend)
        .build
        .send(akkaHttpBackend)
        .httpResponse,
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

  private def substituteCredentialFromTokens(
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

  def withAuthentication(
      authenticationConfig: Option[RestonomerAuthentication],
      akkaHttpBackend: SttpBackend[Future, Any]
  ): RestonomerRequestBuilder = {
    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          restonomerAuthentication.token
            .map { tokenConfig =>
              implicit val tokens: Map[String, String] = getTokensMap(tokenConfig, akkaHttpBackend)

              restonomerAuthentication match {
                case basicAuthentication @ BasicAuthentication(_, basicToken, userName, password) =>
                  basicAuthentication.copy(
                    basicToken = basicToken.map(substituteCredentialFromTokens),
                    userName = userName.map(substituteCredentialFromTokens),
                    password = password.map(substituteCredentialFromTokens)
                  )

                case bearerAuthentication @ BearerAuthentication(_, bearerToken) =>
                  bearerAuthentication.copy(
                    bearerToken = substituteCredentialFromTokens(bearerToken)
                  )

                case apiKeyAuthentication @ APIKeyAuthentication(_, apiKeyName, apiKeyValue, _) =>
                  apiKeyAuthentication.copy(
                    apiKeyName = substituteCredentialFromTokens(apiKeyName),
                    apiKeyValue = substituteCredentialFromTokens(apiKeyValue)
                  )

                case jwtAuthentication @ JWTAuthentication(_, subject, secretKey, _, _) =>
                  jwtAuthentication.copy(
                    subject = substituteCredentialFromTokens(subject),
                    secretKey = substituteCredentialFromTokens(secretKey)
                  )
              }

            }
            .getOrElse(restonomerAuthentication)
            .validateCredentialsAndAuthenticate(httpRequest)
        }
        .getOrElse(httpRequest)
    )
  }

  def withHeaders(headers: Map[String, String]): RestonomerRequestBuilder =
    copy(httpRequest = httpRequest.headers(headers))

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
