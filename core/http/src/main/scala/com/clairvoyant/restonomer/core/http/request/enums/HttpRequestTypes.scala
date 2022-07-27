package com.clairvoyant.restonomer.core.http.request.enums

import com.clairvoyant.restonomer.core.http.request.types.{HttpRequest, SimpleHttpRequest}
import com.clairvoyant.restonomer.core.model.config.RequestConfig

object HttpRequestTypes extends Enumeration {
  val SIMPLE_HTTP_REQUEST: HttpRequestTypes.Value = Value("SimpleHttpRequest")

  def apply(requestConfig: RequestConfig): HttpRequest =
    if (isValidHttpRequestType(requestConfig.requestType))
      (withName(requestConfig.requestType) match {
        case SIMPLE_HTTP_REQUEST =>
          SimpleHttpRequest
      })(requestConfig)
    else
      throw new IllegalArgumentException(s"The request-type: ${requestConfig.requestType} is not supported.")

  def isValidHttpRequestType(requestType: String): Boolean = values.exists(_.toString == requestType)
}
