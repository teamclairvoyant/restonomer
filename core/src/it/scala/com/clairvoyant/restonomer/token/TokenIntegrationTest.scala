package com.clairvoyant.restonomer.token

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class TokenIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "token"

  it should "trigger the token request and get the required credential" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_request.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_token_request.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
