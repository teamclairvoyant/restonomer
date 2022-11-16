package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.{BasicAuthentication, BearerAuthentication, RestonomerAuthentication}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.Request

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withAuthentication(authenticationConfig: Option[RestonomerAuthentication] = None): RestonomerRequestBuilder = {
    // TODO: Implement this method
    def substituteCredentialFromTokenRequestResponse(
        credential: String
    )(implicit tokenRequestResponseMap: Map[String, String]): String = {
      // TODO: get the credential value only if the credential is matching the specific format
      tokenRequestResponseMap.get(credential) match {
        case Some(value) =>
          value
        case None =>
          throw new RestonomerException(
            s"Could not find the value of $credential in the token request response: $tokenRequestResponseMap"
          )
      }
    }

    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          restonomerAuthentication.tokenRequestResponse
            .map { implicit tokenRequestResponseMap =>
              restonomerAuthentication match {
                // TODO: How to force adding here if new authentication is introduced ?
                case basicAuthentication @ BasicAuthentication(basicToken, userName, password) =>
                  basicAuthentication.copy(
                    basicToken = basicToken.map(substituteCredentialFromTokenRequestResponse),
                    userName = userName.map(substituteCredentialFromTokenRequestResponse),
                    password = password.map(substituteCredentialFromTokenRequestResponse)
                  )

                case bearerAuthentication @ BearerAuthentication(bearerToken) =>
                  bearerAuthentication.copy(
                    bearerToken = substituteCredentialFromTokenRequestResponse(bearerToken)
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
