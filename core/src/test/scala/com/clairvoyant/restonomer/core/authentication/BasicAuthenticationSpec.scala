package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.Request

class BasicAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    val authentication = BasicAuthentication()

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain either basicToken or both user-name & password."
  }

  "validateCredentials - with only user-name" should "throw RestonomerContextException" in {
    val authentication = BasicAuthentication(userName = Some("test_user"))

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the password."
  }

  "validateCredentials - with only password" should "throw RestonomerContextException" in {
    val authentication = BasicAuthentication(password = Some("test_password"))

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the user-name."
  }

  "validateCredentials - with only basic-token" should "not throw RestonomerContextException" in {
    val authentication = BasicAuthentication(basicToken = Some("test_token"))

    noException should be thrownBy authentication.validateCredentials()
  }

  "validateCredentials - with only user-name and password" should "not throw RestonomerContextException" in {
    val authentication = BasicAuthentication(userName = Some("test_user"), password = Some("test_password"))

    noException should be thrownBy authentication.validateCredentials()
  }

  "authenticate - with basic-token" should "return the authenticated request" in {
    val authentication = BasicAuthentication(basicToken = Some("test_token"))

    authentication.authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

  "authenticate - with user-name and password" should "return the authenticated request" in {
    val authentication = BasicAuthentication(userName = Some("test_user"), password = Some("test_password"))

    authentication.authenticate(basicHttpRequest) shouldBe a[Request[_, _]]
  }

}
