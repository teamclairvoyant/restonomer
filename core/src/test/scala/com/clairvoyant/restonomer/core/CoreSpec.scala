package com.clairvoyant.restonomer.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import sttp.client3._
import sttp.model.Method

trait CoreSpec extends AnyFlatSpec with Matchers {
  val resourcesPath = "core/src/test/resources"

  val protocol = "http://"
  val host = "localhost"
  val port = 8080
  val url = "/test_url"

  val uri = s"http://$host:$port$url"

  val basicHttpRequest: Request[Either[String, String], Any] = basicRequest.method(
    method = Method.GET,
    uri = uri"$uri"
  )

}
