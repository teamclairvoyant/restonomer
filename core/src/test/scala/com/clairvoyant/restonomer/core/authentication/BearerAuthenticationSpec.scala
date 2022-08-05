package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CredentialConfig
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class BearerAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig()
    val bearerAuthentication = new BearerAuthentication(credentialConfig)

    the[RestonomerContextException] thrownBy bearerAuthentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain bearer-token."
  }

  "validateCredentials - with bearer-token" should "not throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(bearerToken = Some("test_token"))
    val bearerAuthentication = new BearerAuthentication(credentialConfig)

    noException should be thrownBy bearerAuthentication.validateCredentials()
  }

  "authenticate - with bearer-token" should "return the authenticated request" in {
    val credentialConfig = CredentialConfig(bearerToken = Some("test_token"))
    val bearerAuthentication = new BearerAuthentication(credentialConfig)

    bearerAuthentication.authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

}
