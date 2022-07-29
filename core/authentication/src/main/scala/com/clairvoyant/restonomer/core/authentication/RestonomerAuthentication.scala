package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.backend.RestonomerBackend
import com.clairvoyant.restonomer.core.common.enums.AuthenticationTypes._
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.AuthenticationConfig

abstract class RestonomerAuthentication(restonomerBackend: RestonomerBackend) {
  def authenticate: restonomerBackend.RequestR
}

object RestonomerAuthentication {

  def apply(
      restonomerBackend: RestonomerBackend,
      authenticationConfig: AuthenticationConfig
  ): RestonomerAuthentication = {
    val authenticationType = authenticationConfig.authenticationType
    if (isValidAuthenticationType(authenticationType)) {
      withName(authenticationType) match {
        case BASIC_AUTHENTICATION =>
          new BasicAuthentication(restonomerBackend, authenticationConfig.credentials)
      }
    } else {
      throw new RestonomerContextException(s"The authentication-type: $authenticationType is not supported.")
    }
  }

}
