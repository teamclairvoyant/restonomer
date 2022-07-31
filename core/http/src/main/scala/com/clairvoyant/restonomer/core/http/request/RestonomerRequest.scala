package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.{AuthenticationConfig, RequestConfig}
import sttp.client3.{Request, _}
import sttp.model.Method

case class RestonomerRequest(httpRequest: Request[Either[String, String], Any]) {

  def authenticate(authenticationConfig: Option[AuthenticationConfig]): RestonomerRequest =
    this.copy(httpRequest =
      authenticationConfig
        .map(RestonomerAuthentication(_).validateCredentialsAndAuthenticate(httpRequest))
        .getOrElse(httpRequest)
    )

  def send(httpBackendType: Option[String]): RestonomerResponse =
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

  def apply(requestConfig: RequestConfig): RestonomerRequest =
    RestonomerRequest(
      basicRequest.method(
        method = requestConfig.method.map(Method(_)).getOrElse(Method.GET),
        uri = uri"${requestConfig.url}"
      )
    )

}
