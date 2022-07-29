package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.{Identity, Response}

class BasicRequestWithAuthentication(requestConfig: RequestConfig) extends BasicRequest(requestConfig) {

  override val restonomerAuthentication: Option[RestonomerAuthentication] = requestConfig.authentication
    .map(RestonomerAuthentication(_))

  override def send(): Identity[Response[Either[String, String]]] =
    restonomerAuthentication
      .map(_.authenticate(httpRequest).send(HttpBackendTypes(requestConfig.httpBackendType)))
      .get

}
