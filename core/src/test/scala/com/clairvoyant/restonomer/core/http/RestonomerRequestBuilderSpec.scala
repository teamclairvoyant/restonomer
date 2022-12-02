package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.authentication.{BasicAuthentication, BearerAuthentication}
import com.clairvoyant.restonomer.core.common.{CoreSpec, HttpMockSpec}
import com.clairvoyant.restonomer.core.model.{RequestConfig, TokenConfig, TokenResponseConfig}
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, stubFor, urlPathEqualTo}
import sttp.client3.UriContext
import sttp.model.Header
import sttp.model.HeaderNames.Authorization

class RestonomerRequestBuilderSpec extends CoreSpec with HttpMockSpec {

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

  "withAuthentication - with token request" should "return RestonomerRequestBuilder object with the new authenticated httpRequest" in {
    val token = Some(
      TokenConfig(
        tokenRequest = RequestConfig(
          url = uri"$uri",
          authentication = Some(BearerAuthentication(bearerToken = "bearer_token_123"))
        ),
        tokenResponse = TokenResponseConfig(placeholder = "ResponseBody")
      )
    )

    val tokenRequestResponseBody =
      """
        |{
        |  "basic_token": "token_xyz"
        |}
        |""".stripMargin

    val authentication = Some(
      BasicAuthentication(
        token = token,
        basicToken = Some("token[basic_token]")
      )
    )

    stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(tokenRequestResponseBody)))

    RestonomerRequestBuilder(basicHttpRequest)
      .withAuthentication(authentication)
      .httpRequest
      .headers
      .exists(header => header.name == Authorization && header.value == "Basic token_xyz") shouldBe true
  }

  "withHeaders - with custom headers" should "be added to the request" in {
    val headers = Map("header_key" -> "header_value")
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withHeaders(headers)

    restonomerRequestBuilder.httpRequest.headers.contains(Header(headers.head._1, headers.head._2)) shouldBe true
  }

  "withBody - with custom Body" should "be added to the request" in {
    val body = "body_value"
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withBody(Option (body))

    restonomerRequestBuilder.httpRequest.body.show shouldBe s"string: $body"
  }

  "withBody - with No Body" should "be added to the request as an empty string" in {
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest).withBody(None)

    restonomerRequestBuilder.httpRequest.body.show shouldBe "empty"
  }

  "build" should "return RestonomerRequestObject" in {
    RestonomerRequestBuilder(basicHttpRequest).build shouldBe a[RestonomerRequest]
  }

}
