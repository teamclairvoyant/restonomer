package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.common.{CoreSpec, HttpMockSpec}
import com.clairvoyant.restonomer.core.model.RequestConfig
import com.github.tomakehurst.wiremock.client.WireMock._
import sttp.client3._
import sttp.model.Method

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RestonomerRequestSpec extends CoreSpec with HttpMockSpec {

  "builder" should "return RestonomerRequestBuilder object" in {
    val requestConfig = RequestConfig(
      method = Method.GET,
      url = uri"https://test-domain/url"
    )

    RestonomerRequest.builder(requestConfig) shouldBe a[RestonomerRequestBuilder]
    RestonomerRequest.builder(requestConfig).httpRequest shouldBe a[Request[_, _]]
  }

  "send" should "return RestonomerResponse" in {
    val restonomerResponse = new RestonomerRequest(basicHttpRequest).send()

    restonomerResponse shouldBe a[RestonomerResponse]
    restonomerResponse.httpResponse shouldBe a[Future[Response[Either[String, String]]]]
  }

  "send" should "return RestonomerResponse with the mocked response body" in {
    val responseBody = "Sample Response Body"

    stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(responseBody)))

    val restonomerResponse = new RestonomerRequest(basicHttpRequest).send()

    restonomerResponse.httpResponse.foreach(_.body.getOrElse() shouldBe responseBody)
  }

}
