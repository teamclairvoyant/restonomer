package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.BasicAuthentication
import com.clairvoyant.restonomer.core.body.{FormDataBody, TextDataBody}
import com.clairvoyant.restonomer.core.common.{CoreSpec, HttpMockSpec}
import sttp.model.Header

class RestonomerRequestBuilderSpec extends CoreSpec with HttpMockSpec {

  given tokenFunction: Option[String => String] = None

  "withAuthentication - without authenticationConfig" should "return RestonomerRequestBuilder object with the same httpRequest" in {
    val authentication = None
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest should be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "withAuthentication - with authenticationConfig" should "return RestonomerRequestBuilder object with the new authenticated httpRequest" in {
    val authentication = Some(BasicAuthentication(basicToken = Some("test_token")))
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest shouldNot be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "withHeaders - with custom headers" should "be added to the request" in {
    val headers = Map("header_key" -> "header_value")
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withHeaders(headers)

    restonomerRequestBuilder.httpRequest.headers.contains(Header(headers.head._1, headers.head._2)) shouldBe true
  }

  "withBody - with custom text data body" should "be added to the request" in {
    val body = TextDataBody(data = "body_value")

    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(Some(body))
      .httpRequest
      .body
      .show shouldBe s"string: body_value"
  }

  "withBody - with custom form data body" should "be added to the request" in {
    val body = FormDataBody(data = Map("k1" -> "v1", "k2" -> "v2"))

    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(Some(body))
      .httpRequest
      .body
      .show shouldBe s"string: k1=v1&k2=v2"
  }

  "withBody - with no body" should "be added to the request as an empty string" in {
    RestonomerRequestBuilder(basicHttpRequest)
      .withBody(None)
      .httpRequest
      .body
      .show shouldBe "empty"
  }

  "build" should "return RestonomerRequestObject" in {
    RestonomerRequestBuilder(basicHttpRequest).build shouldBe a[RestonomerRequest]
  }

}
