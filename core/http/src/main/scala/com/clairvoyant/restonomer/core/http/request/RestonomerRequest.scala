package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.AuthenticationConfig
import sttp.client3._

case class RestonomerRequest(httpRequest: Request[Either[String, String], Any]) {

  def authenticate(authenticationConfig: Option[AuthenticationConfig]): RestonomerRequest =
    this.copy(httpRequest =
      authenticationConfig
        .map(RestonomerAuthentication(_).authenticate(httpRequest))
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
