package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.exception.RestonomerException

class JWTAuthenticationSpec extends DataScalaxyTestUtil {

  val subject = "test_subject"
  val secretKey = "test_key"

  val jwtAuthentication: JWTAuthentication = JWTAuthentication(
    subject = subject,
    secretKey = secretKey
  )

  "validateCredentials - with blank subject" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(subject = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain both subject and secret-key."
    )
  }

  "validateCredentials - with blank secret-key" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(secretKey = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain both subject and secret-key."
    )
  }

  "validateCredentials - with invalid algorithm" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(algorithm = "invalidalgo")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal("The provided algorithm: invalidalgo is not supported.")
  }

}
