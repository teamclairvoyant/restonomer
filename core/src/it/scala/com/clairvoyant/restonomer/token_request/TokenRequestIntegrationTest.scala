package com.clairvoyant.restonomer.token_request

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class TokenRequestIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "token_request"

  it should "trigger the token request and get the required credential" in {
    runCheckpoint(checkpointFileName = "checkpoint_token_request.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_token_request.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
