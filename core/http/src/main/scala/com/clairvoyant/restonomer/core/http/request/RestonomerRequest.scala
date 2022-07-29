package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._
import sttp.model.Method

class RestonomerRequest(requestConfig: RequestConfig) {

  val httpBackend: SttpBackend[Identity, Any] = HttpBackendTypes(requestConfig.httpBackendType)

  val restonomerAuthentication: Option[RestonomerAuthentication] = requestConfig.authentication
    .map(RestonomerAuthentication(_))

  val httpRequest: Request[Either[String, String], Any] = basicRequest
    .method(
      method = Method(requestConfig.method),
      uri = uri"${requestConfig.url}"
    )

  def send(): RestonomerResponse = {
    RestonomerResponse(
      restonomerAuthentication
        .map(_.authenticate(httpRequest))
        .getOrElse(httpRequest)
        .send(httpBackend)
    )
  }

}

object RestonomerRequest {
  def apply(requestConfig: RequestConfig): RestonomerRequest = new RestonomerRequest(requestConfig)
}
