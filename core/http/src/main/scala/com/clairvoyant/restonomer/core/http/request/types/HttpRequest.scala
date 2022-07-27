package com.clairvoyant.restonomer.core.http.request.types

import sttp.client3.Request

trait HttpRequest {

  def build(): Request[Either[String, String], Any]

}
