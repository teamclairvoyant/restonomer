package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import sttp.client3.Request

case class RestonomerRequestBuilder(httpRequest: Request[Either[String, String], Any]) {

  def withAuthentication(authenticationConfig: Option[RestonomerAuthentication] = None): RestonomerRequestBuilder =
    copy(httpRequest =
      authenticationConfig
        .map(_.validateCredentialsAndAuthenticate(httpRequest))
        .getOrElse(httpRequest)
    )

  def withHeaders(headers: Map[String, String]): RestonomerRequestBuilder = {
    copy(httpRequest = httpRequest.headers(headers))
  }
  
  def withBody(body: Option[String] = None): RestonomerRequestBuilder= {
    copy(httpRequest = body.map(httpRequest.body()).getOrElse(httpRequest)) }
  

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)
}
