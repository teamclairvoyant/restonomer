package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.{CoreSpec, HttpMockSpec}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, stubFor, urlEqualTo}

class OAuth2AuthenticationSpec extends CoreSpec with HttpMockSpec {

  "ClientCredentials - validateCredentials - with empty tokenUrl" should "throw RestonomerException" in {
    val clientCredentials = ClientCredentials(
      tokenUrl = "",
      clientId = "testClientID",
      clientSecret = "testClientSecret"
    )

    val thrown = the[RestonomerException] thrownBy clientCredentials.validateCredentials()

    thrown.getMessage should equal("The provided credentials are invalid. Please provide token-url.")
  }

  "ClientCredentials - validateCredentials - with empty clientId" should "throw RestonomerException" in {
    val clientCredentials = ClientCredentials(
      tokenUrl = "https://localhost:8080/token",
      clientId = "",
      clientSecret = "testClientSecret"
    )

    val thrown = the[RestonomerException] thrownBy clientCredentials.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid client-id."
    )
  }

  "ClientCredentials - validateCredentials - with empty clientSecret" should "throw RestonomerException" in {
    val clientCredentials = ClientCredentials(
      tokenUrl = "https://localhost:8080/token",
      clientId = "testClientID",
      clientSecret = ""
    )

    val thrown = the[RestonomerException] thrownBy clientCredentials.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid client-secret."
    )
  }

  "ClientCredentials - getAccessToken" should "return the mocked access token" in {
    val clientCredentials = ClientCredentials(
      tokenUrl = "http://localhost:8080/token",
      clientId = "testClientID",
      clientSecret = "testClientSecret"
    )

    stubFor(
      WireMock
        .post(urlEqualTo("/token"))
        .willReturn(
          aResponse()
            .withBody("""
                        |{
                        |  "access_token": "test_token"
                        |}
                        |""".stripMargin)
        )
    )

    clientCredentials.getAccessToken shouldBe "test_token"
  }

}
