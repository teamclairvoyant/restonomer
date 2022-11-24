package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException

class JWTAuthenticationSpec extends CoreSpec {

  val subject = "test_subject"
  val secretKey = "test_key"

  val jwtAuthentication: JWTAuthentication = JWTAuthentication(
    subject = subject,
    secretKey = secretKey
  )

  "validateCredentials - with blank subject" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(subject = "")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain both subject and secret-key."
  }

  "validateCredentials - with blank secret-key" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(secretKey = "")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain both subject and secret-key."
  }

  "validateCredentials - with invalid algorithm" should "throw RestonomerException" in {
    val authentication = jwtAuthentication.copy(algorithm = "invalidalgo")

    the[RestonomerException] thrownBy authentication.validateCredentials() should have message
      "The provided algorithm: invalidalgo is not supported."
  }

}
