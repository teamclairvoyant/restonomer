package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.TokenRequestHandler._
import com.clairvoyant.restonomer.core.authentication._
import sttp.client3.{Request, SttpBackend}

import scala.concurrent.Future

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withAuthentication(
      authenticationConfig: Option[RestonomerAuthentication]
  )(implicit akkaHttpBackend: SttpBackend[Future, Any]): RestonomerRequestBuilder = {
    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          restonomerAuthentication.token
            .map { tokenConfig =>
              implicit val tokensMap: Map[String, String] = getTokensMap(tokenConfig)

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
