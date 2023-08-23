package com.clairvoyant.restonomer.common

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.*
import sttp.model.Method

trait CoreSpec extends AnyFlatSpec with Matchers {

  val resourcesPath = "src/test/resources"
  val url = "/test_url"
  val uri = s"http://localhost:8080$url"

  val basicHttpRequest = basicRequest.method(
    method = Method.GET,
    uri = uri"$uri"
  )

}
