package com.clairvoyant.restonomer.core.common

object AuthenticationTypes extends Enumeration {
  val BASIC_AUTHENTICATION: AuthenticationTypes.Value = Value("BasicAuthentication")
  val BEARER_AUTHENTICATION: AuthenticationTypes.Value = Value("BearerAuthentication")

  def isValidAuthenticationType(authenticationType: String): Boolean = values.exists(_.toString == authenticationType)
}
