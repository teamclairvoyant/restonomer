package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.{AuthenticationConfig, CredentialConfig}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class RestonomerAuthenticationSpec extends CoreSpec {

  it should "return BasicAuthentication instance" in {
    val credentialConfig: CredentialConfig = CredentialConfig(
      basicToken = Some("sample_token")
    )

    val authenticationConfig: AuthenticationConfig = AuthenticationConfig(
      authenticationType = "BasicAuthentication",
      credentials = credentialConfig
    )

    RestonomerAuthentication(authenticationConfig) shouldBe a[BasicAuthentication]
  }

  it should "return BearerAuthentication instance" in {
    val credentialConfig: CredentialConfig = CredentialConfig(
      bearerToken = Some("sample_token")
    )

    val authenticationConfig: AuthenticationConfig = AuthenticationConfig(
      authenticationType = "BearerAuthentication",
      credentials = credentialConfig
    )

    RestonomerAuthentication(authenticationConfig) shouldBe a[BearerAuthentication]
  }

  it should "throw RestonomerContextException" in {
    val credentialConfig: CredentialConfig = CredentialConfig(
      bearerToken = Some("sample_token")
    )

    val authenticationConfig: AuthenticationConfig = AuthenticationConfig(
      authenticationType = "ABCDAuthentication",
      credentials = credentialConfig
    )

    val thrown = the[RestonomerContextException] thrownBy RestonomerAuthentication(authenticationConfig)
    thrown.getMessage should be("The authentication-type: ABCDAuthentication is not supported.")
  }

}
