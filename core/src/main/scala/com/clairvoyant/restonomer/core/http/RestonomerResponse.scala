package com.clairvoyant.restonomer.core.http

import sttp.client3.Response

import scala.concurrent.Future

case class RestonomerResponse(httpResponse: Future[Response[Either[String, String]]])
