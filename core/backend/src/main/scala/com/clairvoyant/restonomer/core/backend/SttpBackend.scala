package com.clairvoyant.restonomer.core.backend

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.CredentialConfig
import sttp.client3._

class SttpBackend extends RestonomerBackend {
  override type RequestR = RequestT[Empty, Either[String, String], Any]

  override val httpRequest: RequestR = basicRequest

  override def authenticate(credentialConfig: CredentialConfig): RequestR =
    httpRequest.auth
      .basic(
        user = credentialConfig.userName
          .getOrElse(throw new RestonomerContextException(s"user-name is not provided.")),
        password = credentialConfig.password
          .getOrElse(throw new RestonomerContextException(s"password is not provided."))
      )

}
