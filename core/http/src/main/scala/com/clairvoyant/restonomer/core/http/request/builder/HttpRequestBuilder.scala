package com.clairvoyant.restonomer.core.http.request.builder

import com.clairvoyant.restonomer.core.common.enums.HttpRequestType
import com.clairvoyant.restonomer.core.common.enums.HttpRequestType.SIMPLE_HTTP_REQUEST
import com.clairvoyant.restonomer.core.exceptions.RestonomerHttpRequestException
import com.clairvoyant.restonomer.core.http.request.types.SimpleHttpRequest
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._

object HttpRequestBuilder {
  def apply(requestConfig: RequestConfig) = new HttpRequestBuilder(requestConfig)
}

class HttpRequestBuilder(requestConfig: RequestConfig) {

  def buildHttpRequest: Request[Either[String, String], Any] = {
    (HttpRequestType.withName(requestConfig.requestType) match {
      case SIMPLE_HTTP_REQUEST =>
        SimpleHttpRequest
      case _ =>
        throw new RestonomerHttpRequestException(
          s"The requestType: ${requestConfig.requestType} mentioned in request: ${requestConfig.name} is not supported."
        )
    })(requestConfig).build()
  }

}
