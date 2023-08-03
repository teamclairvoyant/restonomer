package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException

class DigestAuthenticationSpec extends DataScalaxyTestUtil {

  "validateCredentials - with empty credentials" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "", password = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain both user-name & password."
    )
  }

  "validateCredentials - with only user-name" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "test_user", password = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid password."
    )
  }

  "validateCredentials - with only password" should "throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "", password = "test_password")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid user-name."
    )
  }

  "validateCredentials - with only user-name and password" should "not throw RestonomerException" in {
    val authentication = DigestAuthentication(userName = "test_user", password = "test_password")
    noException should be thrownBy authentication.validateCredentials()
  }

}
