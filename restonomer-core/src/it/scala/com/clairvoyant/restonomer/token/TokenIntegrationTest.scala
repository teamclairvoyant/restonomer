package com.clairvoyant.restonomer.token

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class TokenIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "token"

  it should "trigger the token request and get the required credential from the token response body" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_response_body.conf")
    outputDF should matchExpectedDataFrame("expected_test_token_response_body.json")
  }

  it should "trigger the token request and get the required credential from the token response headers" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_response_headers.conf")
    outputDF should matchExpectedDataFrame("expected_test_token_response_headers.json")
  }

}
