package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.AuthenticationTypes._
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.AuthenticationConfig
import sttp.client3.Request

trait RestonomerAuthentication {

  def validateCredentials(): Unit

  def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any]

  def validateCredentialsAndAuthenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
    validateCredentials()
    authenticate(httpRequest)
  }

}

object RestonomerAuthentication {

  def apply(authenticationConfig: AuthenticationConfig): RestonomerAuthentication = {
    val authenticationType = authenticationConfig.authenticationType
    if (isValidAuthenticationType(authenticationType)) {
      withName(authenticationType) match {
        case BASIC_AUTHENTICATION =>
          new BasicAuthentication(authenticationConfig.credentials)
      }
    } else {
      throw new RestonomerContextException(s"The authentication-type: $authenticationType is not supported.")
    }
  }

}
