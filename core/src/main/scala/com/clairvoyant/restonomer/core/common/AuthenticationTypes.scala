package com.clairvoyant.restonomer.core.common

object AuthenticationTypes extends Enumeration {
  val BASIC_AUTHENTICATION: AuthenticationTypes.Value = Value("BasicAuthentication")

  def isValidAuthenticationType(authenticationType: String): Boolean = values.exists(_.toString == authenticationType)
}
