package com.clairvoyant.restonomer.core.http

import sttp.client3.{Identity, Response}

case class RestonomerResponse(httpResponse: Identity[Response[Either[String, String]]])
