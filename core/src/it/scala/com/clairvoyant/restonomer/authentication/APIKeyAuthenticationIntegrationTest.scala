package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class APIKeyAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication/api_key_authentication"

  it should "authenticate request with api key authentication using query string" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_query_param.conf")

    val outputDF = readOutputJSON("query_param")

    val expectedDF = readExpectedMockJSON("expected_api_key_authentication_query_param.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "authenticate request with api key authentication using request header" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_request_header.conf")

    val outputDF = readOutputJSON("request_header")

    val expectedDF = readExpectedMockJSON("expected_api_key_authentication_request_header.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "authenticate request with api key authentication using cookie" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_cookie.conf")

    val outputDF = readOutputJSON("cookie")

    val expectedDF = readExpectedMockJSON("expected_api_key_authentication_cookie.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
