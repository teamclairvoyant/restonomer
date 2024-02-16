package com.clairvoyant.restonomer.http

import com.clairvoyant.restonomer.authentication.BasicAuthentication
import com.clairvoyant.restonomer.body.{FormDataBody, JSONDataBody, TextDataBody}
import com.clairvoyant.restonomer.common.{CoreSpec, HttpMockSpec}
import sttp.model.Header
import sttp.model.HeaderNames.ContentType

class RestonomerRequestBuilderSpec extends CoreSpec with HttpMockSpec {

  given tokenFunction: Option[String => String] = None

  "withAuthentication - without authenticationConfig" should "return RestonomerRequestBuilder object with the same httpRequest" in {
    val authentication = None
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder[String]]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest should be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "withAuthentication - with authenticationConfig" should "return RestonomerRequestBuilder object with the new authenticated httpRequest" in {
    val authentication = Some(BasicAuthentication(basicToken = Some("test_token")))
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder[String]]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest shouldNot be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "withHeaders - with custom headers" should "be added to the request" in {
    val headers = Map("header_key" -> "header_value")
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withHeaders(headers)

    restonomerRequestBuilder.httpRequest.headers.contains(Header(headers.head._1, headers.head._2)) shouldBe true
  }

  "withHeaders - with custom headers and token" should "be added to the request" in {
    given tokenFunction: Option[String => String] = Some(x => x + "_test")

    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withHeaders(
      Map("header_key" -> "token[hv]")
    )

    restonomerRequestBuilder.httpRequest.headers.contains(Header("header_key", "hv_test")) shouldBe true
  }

  "withBody - with custom text data body" should "be added to the request" in {
    val body = TextDataBody(data = "body_value")

    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(Some(body))
      .httpRequest
      .body
      .show shouldBe "string: body_value"
  }

  "withBody - with custom form data body" should "be added to the request" in {
    val body = FormDataBody(data = Map("k1" -> "v1", "k2" -> "v2"))

    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(Some(body))
      .httpRequest
      .body
      .show shouldBe "string: k1=v1&k2=v2"
  }

  "withBody - with json data body" should "be added to the request" in {
    val body = JSONDataBody(data = """{"k1": "v1", "k2": "v2"}""")

    val httpRequest =
      RestonomerRequestBuilder(basicHttpRequest)
        .withBody(Some(body))
        .httpRequest

    httpRequest.body.show shouldBe """string: {"k1": "v1", "k2": "v2"}"""
    httpRequest.headers.contains(Header(ContentType, "application/json")) shouldBe true
  }

  "withBody - with no body" should "be added to the request as an empty string" in {
    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(None)
      .httpRequest
      .body
      .show shouldBe "empty"
  }

  "build" should "return RestonomerRequestObject" in {
    RestonomerRequestBuilder(basicHttpRequest).build shouldBe a[RestonomerRequest[String]]
  }

}
