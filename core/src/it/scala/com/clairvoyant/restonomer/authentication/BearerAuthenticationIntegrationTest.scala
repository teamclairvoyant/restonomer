package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BearerAuthenticationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "authentication/bearer_authentication"

  it should "authenticate request with bearer authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_body.conf")

    val outputDF = readOutputJSON()

    val expectedDF = readExpectedMockJSON("expected_bearer_authentication.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
