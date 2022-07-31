package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.CredentialConfig
import sttp.client3.Request

class BasicAuthentication(credentialConfig: CredentialConfig) extends RestonomerAuthentication {

  val token: Option[String] = credentialConfig.token
  val userName: Option[String] = credentialConfig.userName
  val password: Option[String] = credentialConfig.password

  override def validateCredentials(): Unit = {
    println("Validating credentials...")

    if (token.isEmpty && userName.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain either token or both user-name & password."
      )
    else if (token.isEmpty && userName.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain the user-name."
      )
    else if (token.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain the password."
      )
    else
      println("Credentials are validated.")
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    token
      .map(
        httpRequest.auth
          .basicToken(_)
      )
      .getOrElse(
        httpRequest.auth
          .basic(
            user = credentialConfig.userName.get,
            password = credentialConfig.password.get
          )
      )
  }

}
