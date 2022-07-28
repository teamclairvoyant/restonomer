package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.model.config.RequestConfig

object SimpleHttpRequestWithAuthentication {
  def apply(requestConfig: RequestConfig) = new SimpleHttpRequestWithAuthentication(requestConfig)
}

class SimpleHttpRequestWithAuthentication(requestConfig: RequestConfig) extends SimpleHttpRequest(requestConfig) {}
