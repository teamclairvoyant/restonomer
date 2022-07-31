package com.clairvoyant.restonomer.core.http.response

import sttp.client3.{Identity, Response}

case class RestonomerResponse(httpResponse: Identity[Response[Either[String, String]]])
