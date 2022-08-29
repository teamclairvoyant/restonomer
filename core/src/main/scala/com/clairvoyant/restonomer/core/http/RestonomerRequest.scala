package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.common.HttpBackendTypes
import sttp.client3._
import sttp.model.Method

class RestonomerRequest(httpRequest: Request[Either[String, String], Any]) {

  def send(httpBackendType: Option[String] = None): RestonomerResponse =
    RestonomerResponse(
      httpRequest
        .send(
          httpBackendType
            .map(HttpBackendTypes(_))
            .getOrElse(HttpClientSyncBackend())
        )
    )

}

object RestonomerRequest {

  def builder(method: Option[String], url: String): RestonomerRequestBuilder =
    RestonomerRequestBuilder(
      basicRequest.method(
        method = method.map(Method(_)).getOrElse(Method.GET),
        uri = uri"$url"
      )
    )

}
