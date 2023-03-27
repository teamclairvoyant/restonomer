package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication._
import sttp.client3.Request

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withQueryParams(queryParams: Map[String, String])(
      using tokenFunction: Option[String => String]
  ): RestonomerRequestBuilder =
    copy(httpRequest =
      httpRequest.method(
        method = httpRequest.method,
        uri = httpRequest.uri.withParams(
          tokenFunction
            .map(f => queryParams.view.mapValues(TokenSubstitutor(f).substitute).toMap)
            .getOrElse(queryParams)
        )
      )
    )

  def withAuthentication(
      authenticationConfig: Option[RestonomerAuthentication]
  )(using tokenFunction: Option[String => String]): RestonomerRequestBuilder =
    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          tokenFunction
            .map { f =>
              val tokenSubstitutor = TokenSubstitutor(f)

              restonomerAuthentication match {
                case basicAuthentication @ BasicAuthentication(basicToken, userName, password) =>
                  basicAuthentication.copy(
                    basicToken = basicToken.map(tokenSubstitutor.substitute),
                    userName = userName.map(tokenSubstitutor.substitute),
                    password = password.map(tokenSubstitutor.substitute)
                  )

                case bearerAuthentication @ BearerAuthentication(bearerToken) =>
                  bearerAuthentication.copy(
                    bearerToken = tokenSubstitutor.substitute(bearerToken)
                  )

                case apiKeyAuthentication @ APIKeyAuthentication(apiKeyName, apiKeyValue, _) =>
                  apiKeyAuthentication.copy(
                    apiKeyName = tokenSubstitutor.substitute(apiKeyName),
                    apiKeyValue = tokenSubstitutor.substitute(apiKeyValue)
                  )

                case jwtAuthentication @ JWTAuthentication(subject, secretKey, _, _) =>
                  jwtAuthentication.copy(
                    subject = tokenSubstitutor.substitute(subject),
                    secretKey = tokenSubstitutor.substitute(secretKey)
                  )

                case digestAuthentication @ DigestAuthentication(userName, password) =>
                  digestAuthentication.copy(
                    userName = tokenSubstitutor.substitute(userName),
                    password = tokenSubstitutor.substitute(password)
                  )

                case oAuth2Authentication @ OAuth2Authentication(_) =>
                  oAuth2Authentication
              }

            }
            .getOrElse(restonomerAuthentication)
            .validateCredentialsAndAuthenticate(httpRequest)
        }
        .getOrElse(httpRequest)
    )

  def withHeaders(headers: Map[String, String]): RestonomerRequestBuilder =
    copy(httpRequest = httpRequest.headers(headers))

  def withBody(body: Option[String] = None): RestonomerRequestBuilder =
    copy(httpRequest = body.map(httpRequest.body(_)).getOrElse(httpRequest))

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
