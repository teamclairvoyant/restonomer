package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.{APIKeyPlaceholders, CoreSpec}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.model.{Header, HeaderNames}
import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class APIKeyAuthenticationSpec extends CoreSpec with DataScalaxyTestUtil {

  val apiKeyName = "test_api_key_name"
  val apiKeyValue = "test_api_key_value"

  val apiKeyAuthentication: APIKeyAuthentication = APIKeyAuthentication(
    apiKeyName = apiKeyName,
    apiKeyValue = apiKeyValue,
    placeholder = APIKeyPlaceholders.QueryParam.toString
  )

  "validateCredentials - with blank api-key-name" should "throw RestonomerException" in {
    val authentication = apiKeyAuthentication.copy(apiKeyName = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid api-key-name."
    )

  }

  "validateCredentials - with blank api-key-value" should "throw RestonomerException" in {
    val authentication = apiKeyAuthentication.copy(apiKeyValue = "")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The credentials should contain valid api-key-value."
    )
  }

  "validateCredentials - with invalid placeholder" should "throw RestonomerException" in {
    val authentication = apiKeyAuthentication.copy(placeholder = "InvalidPlaceholder")

    val thrown = the[RestonomerException] thrownBy authentication.validateCredentials()

    thrown.getMessage should equal(
      "The provided credentials are invalid. The placeholder: InvalidPlaceholder is not supported."
    )
  }

  "authenticate - with QUERY_PARAM as placeholder" should "return Request object with modified uri" in {
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
