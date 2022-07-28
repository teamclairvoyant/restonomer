package com.clairvoyant.restonomer.core.http.request.types

import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.{basicRequest, Request, UriContext}
import sttp.model.Method

object SimpleHttpRequest {
  def apply(requestConfig: RequestConfig) = new SimpleHttpRequest(requestConfig)
}

class SimpleHttpRequest(requestConfig: RequestConfig) extends HttpRequest {

  override def build(): Request[Either[String, String], Any] =
    basicRequest
      .method(method = Method(requestConfig.method), uri = uri"${requestConfig.url}")

}
