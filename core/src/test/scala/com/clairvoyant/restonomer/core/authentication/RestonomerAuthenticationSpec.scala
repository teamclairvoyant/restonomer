package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.{AuthenticationConfig, CredentialConfig}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class RestonomerAuthenticationSpec extends CoreSpec {

  it should "return BasicAuthentication instance" in {
    val credentialConfig = CredentialConfig(basicToken = Some("sample_token"))

    val authenticationConfig = AuthenticationConfig(
      authenticationType = "BasicAuthentication",
      credentials = credentialConfig
    )

    RestonomerAuthentication(authenticationConfig) shouldBe a[BasicAuthentication]
  }

  it should "return BearerAuthentication instance" in {
    val credentialConfig = CredentialConfig(bearerToken = Some("sample_token"))

    val authenticationConfig = AuthenticationConfig(
      authenticationType = "BearerAuthentication",
      credentials = credentialConfig
    )

    RestonomerAuthentication(authenticationConfig) shouldBe a[BearerAuthentication]
  }

  it should "throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(bearerToken = Some("sample_token"))

    val authenticationConfig = AuthenticationConfig(
      authenticationType = "ABCDAuthentication",
      credentials = credentialConfig
    )

    the[RestonomerContextException] thrownBy RestonomerAuthentication(
      authenticationConfig
    ) should have message "The authentication-type: ABCDAuthentication is not supported."
  }

  "validateCredentialsAndAuthenticate" should "validate credentials and return the authenticated request" in {
    val credentialConfig = CredentialConfig(basicToken = Some("sample_token"))

    val authenticationConfig = AuthenticationConfig(
      authenticationType = "BasicAuthentication",
      credentials = credentialConfig
    )

    RestonomerAuthentication(authenticationConfig)
      .validateCredentialsAndAuthenticate(basicHttpRequest) shouldBe a[Request[Either[String, String], Any]]
  }

}
