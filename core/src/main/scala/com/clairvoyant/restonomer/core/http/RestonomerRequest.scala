package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.model.RequestConfig
import sttp.client3._

import scala.concurrent.Future

case class RestonomerRequest(httpRequest: Request[Either[String, String], Any])

object RestonomerRequest {

  def builder(
      requestConfig: RequestConfig
  )(implicit akkaHttpBackend: SttpBackend[Future, Any]): RestonomerRequestBuilder =
    RestonomerRequestBuilder(
      basicRequest.method(
        method = requestConfig.method,
        uri = requestConfig.url
      )
    )
      .withAuthentication(requestConfig.authentication)
      .withHeaders(requestConfig.headers)

}
