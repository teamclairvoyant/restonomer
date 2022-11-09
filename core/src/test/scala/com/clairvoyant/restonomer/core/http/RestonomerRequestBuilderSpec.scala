package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.authentication.BasicAuthentication
import sttp.model.Header

class RestonomerRequestBuilderSpec extends CoreSpec {

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

  "build" should "return RestonomerRequestObject" in {
    RestonomerRequestBuilder(basicHttpRequest).build shouldBe a[RestonomerRequest]
  }

}
