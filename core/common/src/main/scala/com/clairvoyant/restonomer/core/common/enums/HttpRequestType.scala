package com.clairvoyant.restonomer.core.common.enums

object HttpRequestType extends Enumeration {
  val SIMPLE_HTTP_REQUEST: HttpRequestType.Value = Value("SimpleHttpRequest")

  def apply(requestType: String): HttpRequestType.Value =
    if (isValidHttpRequestType(requestType))
      withName(requestType)
    else
      throw new IllegalArgumentException(s"The requestType: $requestType is not supported.")

  def isValidHttpRequestType(requestType: String): Boolean = values.exists(_.toString == requestType)
}
