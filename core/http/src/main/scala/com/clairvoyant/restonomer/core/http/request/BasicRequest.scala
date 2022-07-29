package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._
import sttp.model.Method

class BasicRequest(requestConfig: RequestConfig) extends RestonomerRequest(requestConfig) {

  override val restonomerAuthentication: Option[RestonomerAuthentication] = None

  override val httpRequest: Request[Either[String, String], Any] = basicRequest.method(
    method = Method(requestConfig.method),
    uri = uri"${requestConfig.url}"
  )

  override def send(): Identity[Response[Either[String, String]]] =
    httpRequest.send(HttpBackendTypes(requestConfig.httpBackendType))

}
