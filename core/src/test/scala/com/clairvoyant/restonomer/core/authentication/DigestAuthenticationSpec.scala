package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException

class DigestAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "", password = "")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain both user-name & password."
  }

  "validateCredentials - with only user-name" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "test_user", password = "")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain valid password."
  }

  "validateCredentials - with only password" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "", password = "test_password")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain valid user-name."
  }

  "validateCredentials - with only user-name and password" should "not throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "test_user", password = "test_password")
    noException should be thrownBy authentication.validateCredentials()
  }
}