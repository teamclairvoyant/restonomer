package com.clairvoyant.restonomer.token

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class TokenIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "token"

  it should "trigger the token request and get the required credential from the token response body" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_response_body.conf")

    val outputDF = readOutputJSON(outputDirectoryName = "response_body")
    val expectedDF = readExpectedMockJSON(fileName = "expected_test_token_response_body.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "trigger the token request and get the required credential from the token response headers" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_response_headers.conf")

    val outputDF = readOutputJSON(outputDirectoryName = "response_headers")
    val expectedDF = readExpectedMockJSON(fileName = "expected_test_token_response_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
