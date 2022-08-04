package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.model._
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.{Identity, Request, Response}

class RestonomerRequestSpec extends CoreSpec {

  "apply" should "return RestonomerRequest object" in {
    val requestConfig = RequestConfig(method = Some("GET"), url = "https://test-domain/url")

    RestonomerRequest(requestConfig) shouldBe a[RestonomerRequest]
    RestonomerRequest(requestConfig).httpRequest shouldBe a[Request[Either[String, String], Any]]
  }

  "authenticate - without authenticationConfig" should "return RestonomerRequest object with the same httpRequest" in {
    val requestConfig = RequestConfig(method = Some("GET"), url = "https://test-domain/url")
    val authenticationConfig = None

    val restonomerRequest = RestonomerRequest(requestConfig)
    val httpRequest = restonomerRequest.httpRequest

    restonomerRequest.authenticate(authenticationConfig) shouldBe a[RestonomerRequest]
    restonomerRequest.authenticate(authenticationConfig).httpRequest should be theSameInstanceAs httpRequest
  }

  "authenticate - with authenticationConfig" should "return RestonomerRequest object with the new authenticated httpRequest" in {
    val requestConfig = RequestConfig(method = Some("GET"), url = "https://test-domain/url")

    val credentialConfig = CredentialConfig(basicToken = Some("test_token"))

    val authenticationConfig = Some(
      AuthenticationConfig(
        authenticationType = "BasicAuthentication",
        credentials = credentialConfig
      )
    )

    val restonomerRequest = RestonomerRequest(requestConfig)
    val httpRequest = restonomerRequest.httpRequest

    restonomerRequest.authenticate(authenticationConfig) shouldBe a[RestonomerRequest]
    restonomerRequest.authenticate(authenticationConfig).httpRequest shouldNot be theSameInstanceAs httpRequest
  }

  "send" should "return RestonomerResponse" in {
    stubFor(
      get(urlPathEqualTo(url))
        .willReturn(aResponse())
    )

    val requestConfig = RequestConfig(method = Some("GET"), url = uri)
    val httpBackendType = None

    val restonomerResponse = RestonomerRequest(requestConfig).send(httpBackendType)

    restonomerResponse shouldBe a[RestonomerResponse]
    restonomerResponse.httpResponse shouldBe a[Identity[Response[Either[String, String]]]]
  }

}
