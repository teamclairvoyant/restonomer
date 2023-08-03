package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.model.Header

class BearerAuthenticationSpec extends CoreSpec with DataScalaxyTestUtil {

  "validateCredentials - with empty credentials" should "throw RestonomerException" in {
    val authentication = BearerAuthentication(bearerToken = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid bearer-token."
    )
  }

  "validateCredentials - with bearer-token" should "not throw RestonomerException" in {
    val authentication = BearerAuthentication(bearerToken = "test_token")

    noException should be thrownBy authentication.validateCredentials()
  }

  "authenticate - with bearer-token" should "return the authenticated request" in {
    val authentication = BearerAuthentication(bearerToken = "test_token")

    authentication
      .authenticate(basicHttpRequest)
      .headers
      .exists(_.equals(Header("Authorization", "Bearer test_token"))) shouldBe true
  }

}
