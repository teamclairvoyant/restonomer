package com.clairvoyant.restonomer.core.http.response

import sttp.client3.{Identity, Response}

class RestonomerResponse(val httpResponse: Identity[Response[Either[String, String]]])

object RestonomerResponse {
  def apply(httpResponse: Identity[Response[Either[String, String]]]) = new RestonomerResponse(httpResponse)
}
