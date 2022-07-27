package com.clairvoyant.restonomer.core.http.request.enums

object HttpRequestTypes extends Enumeration {
  val SIMPLE_HTTP_REQUEST: HttpRequestTypes.Value = Value("SimpleHttpRequest")

  def apply(requestType: String): HttpRequestTypes.Value =
    if (isValidHttpRequestType(requestType))
      withName(requestType)
    else
      throw new IllegalArgumentException(s"The requestType: $requestType is not supported.")

  def isValidHttpRequestType(requestType: String): Boolean = values.exists(_.toString == requestType)
}
