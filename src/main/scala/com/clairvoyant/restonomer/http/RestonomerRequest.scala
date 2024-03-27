package com.clairvoyant.restonomer.http

import com.clairvoyant.restonomer.*
import com.clairvoyant.restonomer.model.*
import sttp.client3.*
import sttp.model.Method

case class RestonomerRequest[T](httpRequest: HttpRequest[T])

object RestonomerRequest {

  def builder[T](
      requestConfig: RequestConfig,
      httpResponseType: HttpResponseAs[T]
  )(using tokenFunction: Option[String => String] = None): RestonomerRequestBuilder[T] =
    RestonomerRequestBuilder[T](
      RequestT[Identity, Either[String, T], Any](
        method = Method(requestConfig.method),
        uri = uri"${requestConfig.url}",
        body = NoBody,
        headers = Vector(),
        response = httpResponseType,
        options = RequestOptions(
          followRedirects = true,
          readTimeout = DefaultReadTimeout,
          maxRedirects = 32,
          redirectToGet = false
        ),
        tags = Map()
      )
    )
      .withQueryParams(requestConfig.queryParams)
      .withAuthentication(requestConfig.authentication)
      .withHeaders(requestConfig.headers)
      .withBody(requestConfig.body)

}
