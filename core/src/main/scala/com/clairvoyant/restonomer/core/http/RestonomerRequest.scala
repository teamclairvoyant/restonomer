package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.common.HttpBackendTypes
import com.clairvoyant.restonomer.core.model.RequestConfig
import sttp.client3._
import sttp.model.Method

class RestonomerRequest(val httpRequest: Request[Either[String, String], Any]) {

  def send(httpBackendType: String = HttpBackendTypes.HTTP_CLIENT_SYNC_BACKEND.toString): RestonomerResponse =
    RestonomerResponse(httpRequest.send(HttpBackendTypes(httpBackendType)))

}

object RestonomerRequest {

  def builder(requestConfig: RequestConfig): RestonomerRequestBuilder =
    RestonomerRequestBuilder(
      basicRequest.method(
        method = Method(requestConfig.method),
        uri = uri"${requestConfig.url}"
      )
    )
      .withAuthentication(requestConfig.authentication)
      .withHeaders(requestConfig.headers)

}
