package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.common.enums.HttpRequestTypes._
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.Request

abstract class HttpRequest {
  def build(): Request[Either[String, String], Any]
}

object HttpRequest {

  def apply(requestConfig: RequestConfig): HttpRequest = {
    if (isValidHttpRequestType(requestConfig.requestType)) {
      withName(requestConfig.requestType) match {
        case SIMPLE_HTTP_REQUEST =>
          SimpleHttpRequest(requestConfig)
        case SIMPLE_HTTP_REQUEST_WITH_AUTHENTICATION =>
          SimpleHttpRequestWithAuthentication(requestConfig)
      }
    } else
      throw new RestonomerContextException(s"The request-type: ${requestConfig.requestType} is not supported.")
  }

}
