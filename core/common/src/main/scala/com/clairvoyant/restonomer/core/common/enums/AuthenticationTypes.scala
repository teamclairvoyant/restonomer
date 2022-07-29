package com.clairvoyant.restonomer.core.common.enums

object AuthenticationTypes extends Enumeration {
  val BASIC_AUTHENTICATION: AuthenticationTypes.Value = Value("BasicAuthentication")

  def isValidAuthenticationType(authenticationType: String): Boolean = values.exists(_.toString == authenticationType)
}
