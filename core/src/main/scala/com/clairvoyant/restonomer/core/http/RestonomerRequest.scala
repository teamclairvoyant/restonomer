package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.common.HttpBackendTypes
import sttp.client3._
import sttp.model.Method

class RestonomerRequest(httpRequest: Request[Either[String, String], Any]) {

  def send(httpBackendType: String): RestonomerResponse =
    RestonomerResponse(httpRequest.send(HttpBackendTypes(httpBackendType)))

}

object RestonomerRequest {

  def builder(method: String, url: String): RestonomerRequestBuilder =
    RestonomerRequestBuilder(
      basicRequest.method(
        method = Method(method),
        uri = uri"$url"
      )
    )

}
