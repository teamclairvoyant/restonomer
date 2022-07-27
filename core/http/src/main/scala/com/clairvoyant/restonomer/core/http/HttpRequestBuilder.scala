package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._
import sttp.model.Method

object HttpRequestBuilder {
  def apply(requestConfig: RequestConfig) = new HttpRequestBuilder(requestConfig)
}

class HttpRequestBuilder(requestConfig: RequestConfig) {

  def buildHttpRequest: Request[Either[String, String], Any] =
    basicRequest
      .method(method = Method(requestConfig.method), uri = uri"${requestConfig.url}")

}
