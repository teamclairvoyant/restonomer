package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.TokenRequestHandler.getToken
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
              val credentialsWithTokenSubstitutor = CredentialsWithTokenSubstitutor(getToken(tokenConfig))

              restonomerAuthentication match {
                case basicAuthentication @ BasicAuthentication(_, basicToken, userName, password) =>
                  basicAuthentication.copy(
                    basicToken = basicToken.map(credentialsWithTokenSubstitutor.substituteCredentialWithToken),
                    userName = userName.map(credentialsWithTokenSubstitutor.substituteCredentialWithToken),
                    password = password.map(credentialsWithTokenSubstitutor.substituteCredentialWithToken)
                  )

                case bearerAuthentication @ BearerAuthentication(_, bearerToken) =>
                  bearerAuthentication.copy(
                    bearerToken = credentialsWithTokenSubstitutor.substituteCredentialWithToken(bearerToken)
                  )

                case apiKeyAuthentication @ APIKeyAuthentication(_, apiKeyName, apiKeyValue, _) =>
                  apiKeyAuthentication.copy(
                    apiKeyName = credentialsWithTokenSubstitutor.substituteCredentialWithToken(apiKeyName),
                    apiKeyValue = credentialsWithTokenSubstitutor.substituteCredentialWithToken(apiKeyValue)
                  )

                case jwtAuthentication @ JWTAuthentication(_, subject, secretKey, _, _) =>
                  jwtAuthentication.copy(
                    subject = credentialsWithTokenSubstitutor.substituteCredentialWithToken(subject),
                    secretKey = credentialsWithTokenSubstitutor.substituteCredentialWithToken(secretKey)
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
    copy(httpRequest = body.map(s => httpRequest.body(s)).getOrElse(httpRequest))
  }

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
