package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CredentialConfig
import sttp.client3.Request

class BearerAuthentication(credentialConfig: CredentialConfig) extends RestonomerAuthentication {
  val bearerToken: Option[String] = credentialConfig.bearerToken

  override def validateCredentials(): Unit = {
    if (bearerToken.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain bearer-token."
      )
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    httpRequest.auth.bearer(bearerToken.get)
  }

}
