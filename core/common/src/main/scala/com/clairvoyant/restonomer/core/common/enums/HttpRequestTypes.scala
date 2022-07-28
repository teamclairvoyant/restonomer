package com.clairvoyant.restonomer.core.common.enums

object HttpRequestTypes extends Enumeration {

  val SIMPLE_HTTP_REQUEST: HttpRequestTypes.Value = Value("SimpleHttpRequest")
  val SIMPLE_HTTP_REQUEST_WITH_AUTHENTICATION: HttpRequestTypes.Value = Value("SimpleHttpRequestWithAuthentication")

  def isValidHttpRequestType(requestType: String): Boolean = values.exists(_.toString == requestType)
}
