package com.clairvoyant.restonomer.http

import com.clairvoyant.restonomer.authentication.*
import com.clairvoyant.restonomer.body.*
import sttp.client3.Request
import sttp.model.Header
import sttp.model.HeaderNames.*

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withQueryParams(queryParams: Map[String, String])(
      using tokenFunction: Option[String => String]
  ): RestonomerRequestBuilder =
    copy(httpRequest =
      httpRequest.method(
        method = httpRequest.method,
        uri = httpRequest.uri.withParams(
          tokenFunction
            .map(f => queryParams.view.mapValues(TokenSubstitutor(f).substitute).toMap)
            .getOrElse(queryParams)
        )
      )
    )

  def withAuthentication(
      authenticationConfig: Option[RestonomerAuthentication]
  )(using tokenFunction: Option[String => String]): RestonomerRequestBuilder =
    copy(httpRequest =
      authenticationConfig
        .map { restonomerAuthentication =>
          tokenFunction
            .map(f => restonomerAuthentication.substituteToken(TokenSubstitutor(f)))
            .getOrElse(restonomerAuthentication)
            .validateCredentialsAndAuthenticate(httpRequest)
        }
        .getOrElse(httpRequest)
    )

  def withHeaders(headers: Map[String, String]) (using tokenFunction: Option[String => String]): RestonomerRequestBuilder =
      copy(httpRequest = httpRequest.headers( tokenFunction
            .map(f => headers.view.mapValues(TokenSubstitutor(f).substitute).toMap)
            .getOrElse(headers)))

  def withBody(body: Option[RestonomerRequestBody] = None): RestonomerRequestBuilder =
    copy(httpRequest =
      body
        .map {
          case TextDataBody(data) => httpRequest.body(data)
          case FormDataBody(data) => httpRequest.body(data)
          case JSONDataBody(data) =>
            httpRequest
              .body(data)
              .header(Header(ContentType, "application/json"), replaceExisting = true)
        }
        .getOrElse(httpRequest)
    )

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
