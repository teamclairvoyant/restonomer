package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class BearerAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    the[RestonomerContextException] thrownBy BearerAuthentication().validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain bearer-token."
  }

  "validateCredentials - with bearer-token" should "not throw RestonomerContextException" in {
    noException should be thrownBy BearerAuthentication(bearerToken = Some("test_token")).validateCredentials()
  }

  "authenticate - with bearer-token" should "return the authenticated request" in {
    BearerAuthentication(bearerToken = Some("test_token")).authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

}
