package com.clairvoyant.restonomer.core.http.request.builder

import com.clairvoyant.restonomer.core.http.request.enums.HttpRequestTypes._
import com.clairvoyant.restonomer.core.http.request.enums.HttpRequestTypes
import com.clairvoyant.restonomer.core.http.request.types.SimpleHttpRequest
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._

object HttpRequestBuilder {
  def apply(requestConfig: RequestConfig) = new HttpRequestBuilder(requestConfig)
}

class HttpRequestBuilder(requestConfig: RequestConfig) {

  def buildHttpRequest: Request[Either[String, String], Any] = {
    (HttpRequestTypes(requestConfig.requestType) match {
      case SIMPLE_HTTP_REQUEST =>
        SimpleHttpRequest
    })(requestConfig).build()
  }

}
