package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.CredentialConfig
import sttp.client3.Request

class BasicAuthentication(credentialConfig: CredentialConfig) extends RestonomerAuthentication {

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] =
    httpRequest.auth.basic(
      user = credentialConfig.userName.getOrElse(throw new RestonomerContextException(s"user-name is not provided.")),
      password = credentialConfig.password.getOrElse(throw new RestonomerContextException(s"password is not provided."))
    )

}

