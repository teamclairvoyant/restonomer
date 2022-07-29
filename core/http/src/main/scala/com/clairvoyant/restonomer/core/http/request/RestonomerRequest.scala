package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.backend.RestonomerBackend
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._

class RestonomerRequest(restonomerBackend: RestonomerBackend, requestConfig: RequestConfig) {
  val httpBackend: SttpBackend[Identity, Any] = HttpBackendTypes(requestConfig.httpBackendType)

  val restonomerAuthentication: Option[RestonomerAuthentication] = requestConfig.authentication
    .map(RestonomerAuthentication(restonomerBackend, _))

  def send(): RestonomerResponse =
    restonomerBackend
      .send(restonomerAuthentication.map(_.authenticate).getOrElse(restonomerBackend.httpRequest), requestConfig)

}

object RestonomerRequest {

  def apply(restonomerBackend: RestonomerBackend, requestConfig: RequestConfig): RestonomerRequest =
    new RestonomerRequest(restonomerBackend, requestConfig)

}
