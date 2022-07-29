package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.model.config.RequestConfig

class BasicRequest(requestConfig: RequestConfig) extends RestonomerRequest(requestConfig) {
  override def authenticate: BasicRequest = this
}
