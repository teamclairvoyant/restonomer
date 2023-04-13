package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class APIKeyAuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/api_key_authentication"

  it should "authenticate request with api key authentication using query string" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_query_param.conf")
    outputDF should matchExpectedDataFrame("expected_api_key_authentication_query_param.json")
  }

  it should "authenticate request with api key authentication using request header" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_request_header.conf")
    outputDF should matchExpectedDataFrame("expected_api_key_authentication_request_header.json")
  }

  it should "authenticate request with api key authentication using cookie" in {
    runCheckpoint(checkpointFileName = "checkpoint_api_key_authentication_cookie.conf")
    outputDF should matchExpectedDataFrame("expected_api_key_authentication_cookie.json")
  }

}
