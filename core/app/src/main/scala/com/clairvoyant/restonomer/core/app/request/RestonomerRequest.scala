package com.clairvoyant.restonomer.core.app.request

import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.request.HttpRequest
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.{Identity, Response}

class RestonomerRequest(httpRequest: HttpRequest) {

  def send(httpBackendType: String): Identity[Response[Either[String, String]]] =
    httpRequest
      .build()
      .send(HttpBackendTypes(httpBackendType))

}

object RestonomerRequest {

  def apply(requestConfig: RequestConfig): RestonomerRequest = new RestonomerRequest(HttpRequest(requestConfig))

}
