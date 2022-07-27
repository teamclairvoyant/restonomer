package com.clairvoyant.restonomer.core.http.request.builder

import com.clairvoyant.restonomer.core.common.enums.HttpRequestType
import com.clairvoyant.restonomer.core.common.enums.HttpRequestType._
import com.clairvoyant.restonomer.core.http.request.types.SimpleHttpRequest
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._

object HttpRequestBuilder {
  def apply(requestConfig: RequestConfig) = new HttpRequestBuilder(requestConfig)
}

class HttpRequestBuilder(requestConfig: RequestConfig) {

  def buildHttpRequest: Request[Either[String, String], Any] = {
    (HttpRequestType(requestConfig.requestType) match {
      case SIMPLE_HTTP_REQUEST =>
        SimpleHttpRequest
    })(requestConfig).build()
  }

}
