package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CredentialConfig
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3.{Request, UriContext, basicRequest}
import sttp.model.Method

class BasicAuthenticationSpec extends CoreSpec {

  "validateCredentials - with empty credentials" should "throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig()
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    the[RestonomerContextException] thrownBy basicAuthentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain either basicToken or both user-name & password."
  }

  "validateCredentials - with only user-name" should "throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(userName = Some("test_user"))
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    the[RestonomerContextException] thrownBy basicAuthentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the password."
  }

  "validateCredentials - with only password" should "throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(password = Some("test_password"))
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    the[RestonomerContextException] thrownBy basicAuthentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain the user-name."
  }

  "validateCredentials - with only basic-token" should "not throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(basicToken = Some("test_token"))
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    noException should be thrownBy basicAuthentication.validateCredentials()
  }

  "validateCredentials - with only user-name and password" should "not throw RestonomerContextException" in {
    val credentialConfig = CredentialConfig(
      userName = Some("test_user"),
      password = Some("test_password")
    )
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    noException should be thrownBy basicAuthentication.validateCredentials()
  }

  "authenticate - with basic-token" should "return the authenticated request" in {
    val credentialConfig = CredentialConfig(basicToken = Some("test_token"))
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    val httpRequest = basicRequest.method(Method.GET, uri"https://test-domain/url")

    basicAuthentication.authenticate(httpRequest) shouldBe a[Request[Either[String, String], Any]]
  }

  "authenticate - with user-name and password" should "return the authenticated request" in {
    val credentialConfig = CredentialConfig(
      userName = Some("test_user"),
      password = Some("test_password")
    )
    val basicAuthentication = new BasicAuthentication(credentialConfig)

    val httpRequest = basicRequest.method(Method.GET, uri"https://test-domain/url")

    basicAuthentication.authenticate(httpRequest) shouldBe a[Request[Either[String, String], Any]]
  }

}
