package com.clairvoyant.restonomer.authentication

import com.amazonaws.auth.internal.SignerConstants.*
import com.clairvoyant.restonomer.common.CoreSpec
import com.clairvoyant.restonomer.exception.RestonomerException
import sttp.model.{Header, HeaderNames}

class AwsSignatureAuthenticationSpec extends CoreSpec {

  "validateCredentials - with blank access-key" should "throw RestonomerException" in {
    val authentication = AwsSignatureAuthentication(
      accessKey = "",
      secretKey = "test_secret_key"
    )

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid access key."
    )

  }

  "validateCredentials - with blank secret-key" should "throw RestonomerException" in {
    val authentication = AwsSignatureAuthentication(
      accessKey = "test_access_key",
      secretKey = ""
    )
    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid secret key."
    )
  }

  "authenticate" should "return Request object with required headers" in {
    val authentication = AwsSignatureAuthentication(
      accessKey = "test_access_key",
      secretKey = "test_secret_key"
    )

    val headers = authentication
      .authenticate(basicHttpRequest)
      .headers
      .map(_.name)

    headers.contains(AUTHORIZATION) shouldBe true
    headers.contains(X_AMZ_DATE) shouldBe true
    headers.contains(X_AMZ_CONTENT_SHA256) shouldBe true
  }

}
