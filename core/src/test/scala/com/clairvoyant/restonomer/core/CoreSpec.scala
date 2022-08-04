package com.clairvoyant.restonomer.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import sttp.client3.{Request, UriContext, basicRequest}
import sttp.model.Method

trait CoreSpec extends AnyFlatSpec with Matchers {
  val resourcesPath = "core/src/test/resources"

  val basicHttpRequest: Request[Either[String, String], Any] = basicRequest.method(
    method = Method.GET,
    uri = uri"https://test-domain/url"
  )

}
