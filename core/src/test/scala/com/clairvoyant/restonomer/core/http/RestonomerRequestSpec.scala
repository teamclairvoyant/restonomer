package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model._
import com.clairvoyant.restonomer.core.{CoreSpec, HttpMockSpec}
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3._

class RestonomerRequestSpec extends CoreSpec with HttpMockSpec {

  "apply" should "return RestonomerRequest object" in {
    val requestConfig = RequestConfig(method = Some("GET"), url = "https://test-domain/url")

    RestonomerRequest(requestConfig) shouldBe a[RestonomerRequest]
    RestonomerRequest(requestConfig).httpRequest shouldBe a[Request[_, _]]
  }

  "authenticate - without authenticationConfig" should "return RestonomerRequest object with the same httpRequest" in {
    val restonomerRequest = RestonomerRequest(basicHttpRequest)
    val httpRequest = restonomerRequest.httpRequest

    restonomerRequest.authenticate() shouldBe a[RestonomerRequest]
    restonomerRequest.authenticate().httpRequest should be theSameInstanceAs httpRequest
  }

  "authenticate - with authenticationConfig" should "return RestonomerRequest object with the new authenticated httpRequest" in {
    val credentialConfig = CredentialConfig(basicToken = Some("test_token"))

    val authenticationConfig = Some(
      AuthenticationConfig(
        authenticationType = "BasicAuthentication",
        credentials = credentialConfig
      )
    )

    val restonomerRequest = RestonomerRequest(basicHttpRequest)
    val httpRequest = restonomerRequest.httpRequest

    restonomerRequest.authenticate(authenticationConfig) shouldBe a[RestonomerRequest]
    restonomerRequest.authenticate(authenticationConfig).httpRequest shouldNot be theSameInstanceAs httpRequest
  }

  "send" should "return RestonomerResponse" in {
    val restonomerResponse = RestonomerRequest(basicHttpRequest).send()

    restonomerResponse shouldBe a[RestonomerResponse]
    restonomerResponse.httpResponse shouldBe a[Identity[_]]
  }

  "send" should "return RestonomerResponse with the mocked response body" in {
    val responseBody = "Sample Response Body"

    stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(responseBody)))

    val restonomerResponse = RestonomerRequest(basicHttpRequest).send(Some("HttpClientSyncBackend"))
    restonomerResponse.httpResponse.body.getOrElse() shouldBe responseBody
  }

  "send with invalid HttpBackendType" should "throw RestonomerContextException" in {
    the[RestonomerContextException] thrownBy RestonomerRequest(basicHttpRequest).send(
      Some("ABCDBackendType")
    ) should have message "The http-backend-type: ABCDBackendType is not supported."
  }

}
