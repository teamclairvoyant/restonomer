package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class APIKeyAuthenticationSpec extends CoreSpec {

  val apiKeyAuthentication: APIKeyAuthentication = APIKeyAuthentication(
    apiKeyName = "test_api_key_name",
    apiKeyValue = "test_api_key_value",
    placeholder = "QueryString"
  )

  "validateCredentials - with blank api-key-name" should "throw RestonomerContextException" in {
    val authentication = apiKeyAuthentication.copy(apiKeyName = "")

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain valid api-key-name."
  }

  "validateCredentials - with blank api-key-value" should "throw RestonomerContextException" in {
    val authentication = apiKeyAuthentication.copy(apiKeyValue = "")

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The credentials should contain valid api-key-value."
  }

  "validateCredentials - with invalid placeholder" should "throw RestonomerContextException" in {
    val authentication = apiKeyAuthentication.copy(placeholder = "InvalidPlaceholder")

    the[RestonomerContextException] thrownBy authentication.validateCredentials() should have message
      "The provided credentials are invalid. The placeholder: InvalidPlaceholder is not supported."
  }

}
