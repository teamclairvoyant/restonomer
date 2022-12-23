package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.model._
import sttp.client3._
import sttp.model.Method

case class RestonomerRequest(httpRequest: Request[Either[String, String], Any])

object RestonomerRequest {

  def builder(
      requestConfig: RequestConfig
  )(implicit tokenFunction: Option[String => String] = None): RestonomerRequestBuilder =
    RestonomerRequestBuilder(
      basicRequest.method(
        method = Method(requestConfig.method),
        uri = uri"${requestConfig.url}"
      )
    )
      .withQueryParams(requestConfig.queryParams)
      .withAuthentication(requestConfig.authentication)
      .withHeaders(requestConfig.headers)
      .withBody(requestConfig.body)

}
