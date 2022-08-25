package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.model.{Header, HeaderNames}

class APIKeyAuthenticationSpec extends CoreSpec {

  val apiKeyName = "test_api_key_name"
  val apiKeyValue = "test_api_key_value"

  val apiKeyAuthentication: APIKeyAuthentication = APIKeyAuthentication(
    apiKeyName = apiKeyName,
    apiKeyValue = apiKeyValue,
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

  "authenticate - with QUERY_STRING as placeholder" should "return Request object with modified uri" in {
    apiKeyAuthentication
      .authenticate(basicHttpRequest)
      .uri
      .paramsMap
      .exists(param => param._1 == apiKeyName && param._2 == apiKeyValue) shouldBe true
  }

  "authenticate - with REQUEST_HEADER as placeholder" should "return Request object with required headers" in {
    apiKeyAuthentication
      .copy(placeholder = "RequestHeader")
      .authenticate(basicHttpRequest)
      .headers
      .contains(Header(apiKeyName, apiKeyValue)) shouldBe true
  }

  "authenticate - with Cookie as placeholder" should "return Request object with required cookies" in {
    apiKeyAuthentication
      .copy(placeholder = "Cookie")
      .authenticate(basicHttpRequest)
      .header(HeaderNames.Cookie)
      .exists(_.equals(s"$apiKeyName=$apiKeyValue")) shouldBe true
  }

}
