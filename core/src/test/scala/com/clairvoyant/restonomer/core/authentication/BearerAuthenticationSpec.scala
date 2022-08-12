package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class BearerAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    val authentication = BearerAuthentication()

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain bearer-token."
  }

  "validateCredentials - with bearer-token" should "not throw RestonomerContextException" in {
    val authentication = BearerAuthentication(bearerToken = Some("test_token"))

    noException should be thrownBy authentication.validateCredentials()
  }

  "authenticate - with bearer-token" should "return the authenticated request" in {
    val authentication = BearerAuthentication(bearerToken = Some("test_token"))

    authentication.authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

}
