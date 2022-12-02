package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.Request

import scala.util.matching.Regex

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withAuthentication(authenticationConfig: Option[RestonomerAuthentication]): RestonomerRequestBuilder = {
    val TOKEN_CREDENTIAL_REGEX_PATTERN: Regex = """token\[(.*)]""".r

    def substituteCredentialFromTokens(
        credential: String
    )(implicit tokens: Map[String, String]): String =
      TOKEN_CREDENTIAL_REGEX_PATTERN
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

    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          restonomerAuthentication.tokensMap
            .map { implicit tokens =>
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

  def withBody(body: Option[String] = None): RestonomerRequestBuilder = {
    copy(httpRequest = body.map(s=>httpRequest.body(s)).getOrElse(httpRequest))
  }

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
