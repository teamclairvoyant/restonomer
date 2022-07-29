package com.clairvoyant.restonomer.core.common.enums

object HttpRequestTypes extends Enumeration {

  val BASIC_REQUEST: HttpRequestTypes.Value = Value("BasicRequest")
  val BASIC_REQUEST_WITH_AUTHENTICATION: HttpRequestTypes.Value = Value("BasicRequestWithAuthentication")

  def isValidHttpRequestType(requestType: String): Boolean = values.exists(_.toString == requestType)
}
