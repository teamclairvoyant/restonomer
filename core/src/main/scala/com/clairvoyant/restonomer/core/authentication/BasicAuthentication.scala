package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CredentialConfig
import sttp.client3.Request

class BasicAuthentication(credentialConfig: CredentialConfig) extends RestonomerAuthentication {

  val basicToken: Option[String] = credentialConfig.basicToken
  val userName: Option[String] = credentialConfig.userName
  val password: Option[String] = credentialConfig.password

  override def validateCredentials(): Unit = {
    if (basicToken.isEmpty && userName.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain either basicToken or both user-name & password."
      )
    else if (basicToken.isEmpty && userName.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain the user-name."
      )
    else if (basicToken.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain the password."
      )
    else
      println("Credentials are validated successfully.")
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    basicToken
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
