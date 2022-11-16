package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.Request

import scala.util.matching.Regex

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withAuthentication(authenticationConfig: Option[RestonomerAuthentication] = None): RestonomerRequestBuilder = {
    val TOKEN_CREDENTIAL_REGEX_PATTERN: Regex = """token\[(.*)]""".r

    def substituteCredentialFromTokenRequestResponse(
        credential: String
    )(implicit tokenRequestResponseMap: Map[String, String]): String = {
      TOKEN_CREDENTIAL_REGEX_PATTERN
        .findFirstMatchIn(credential)
        .map { matcher =>
          tokenRequestResponseMap.get(matcher.group(1)) match {
            case Some(value) =>
              value
            case None =>
              throw new RestonomerException(
                s"Could not find the value of $credential in the token request response: $tokenRequestResponseMap"
              )
          }
        }
        .getOrElse(credential)
    }

    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          restonomerAuthentication.tokenRequestResponse
            .map { implicit tokenRequestResponseMap =>
              restonomerAuthentication match {
                case basicAuthentication @ BasicAuthentication(_, basicToken, userName, password) =>
                  basicAuthentication.copy(
                    basicToken = basicToken.map(substituteCredentialFromTokenRequestResponse),
                    userName = userName.map(substituteCredentialFromTokenRequestResponse),
                    password = password.map(substituteCredentialFromTokenRequestResponse)
                  )

                case bearerAuthentication @ BearerAuthentication(_, bearerToken) =>
                  bearerAuthentication.copy(
                    bearerToken = substituteCredentialFromTokenRequestResponse(bearerToken)
                  )

                case apiKeyAuthentication @ APIKeyAuthentication(_, apiKeyName, apiKeyValue, _) =>
                  apiKeyAuthentication.copy(
                    apiKeyName = substituteCredentialFromTokenRequestResponse(apiKeyName),
                    apiKeyValue = substituteCredentialFromTokenRequestResponse(apiKeyValue)
                  )

                case jwtAuthentication @ JWTAuthentication(_, subject, secretKey, _, _) =>
                  jwtAuthentication.copy(
                    subject = substituteCredentialFromTokenRequestResponse(subject),
                    secretKey = substituteCredentialFromTokenRequestResponse(secretKey)
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
