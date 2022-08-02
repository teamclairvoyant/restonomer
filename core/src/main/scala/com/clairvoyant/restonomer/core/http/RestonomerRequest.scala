package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.HttpBackendTypes
import com.clairvoyant.restonomer.core.model.{AuthenticationConfig, RequestConfig}
import sttp.client3.{HttpClientSyncBackend, Request, UriContext, basicRequest}
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
