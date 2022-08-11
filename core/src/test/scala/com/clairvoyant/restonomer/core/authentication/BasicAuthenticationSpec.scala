package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class BasicAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    the[RestonomerContextException] thrownBy BasicAuthentication().validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain either basicToken or both user-name & password."
  }

  "validateCredentials - with only user-name" should "throw RestonomerContextException" in {
    the[RestonomerContextException] thrownBy
      BasicAuthentication(userName = Some("test_user")).validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the password."
  }

  "validateCredentials - with only password" should "throw RestonomerContextException" in {
    the[RestonomerContextException] thrownBy
      BasicAuthentication(password = Some("test_password")).validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the user-name."
  }

  "validateCredentials - with only basic-token" should "not throw RestonomerContextException" in {
    noException should be thrownBy BasicAuthentication(basicToken = Some("test_token")).validateCredentials()
  }

  "validateCredentials - with only user-name and password" should "not throw RestonomerContextException" in {
    noException should be thrownBy
      BasicAuthentication(userName = Some("test_user"), password = Some("test_password")).validateCredentials()
  }

  "authenticate - with basic-token" should "return the authenticated request" in {
    BasicAuthentication(basicToken = Some("test_token")).authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

  "authenticate - with user-name and password" should "return the authenticated request" in {
    BasicAuthentication(userName = Some("test_user"), password = Some("test_password"))
      .authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

}
