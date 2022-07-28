package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.Request

object SimpleHttpRequestWithAuthentication {
  def apply(requestConfig: RequestConfig) = new SimpleHttpRequestWithAuthentication(requestConfig)
}

class SimpleHttpRequestWithAuthentication(requestConfig: RequestConfig) extends SimpleHttpRequest(requestConfig) {

  override def build(): Request[Either[String, String], Any] =
    super
      .build()
      .auth
      .basic(
        user = requestConfig.authentication
          .map(_.credentials.userName)
          .getOrElse(throw new RestonomerContextException("user-name is not provided.")),
        password = requestConfig.authentication
          .map(_.credentials.password)
          .getOrElse(throw new RestonomerContextException("password is not provided."))
      )

}
