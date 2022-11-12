package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BasicAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication/basic_authentication"

  it should "authenticate request with basic authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_basic_authentication_token.conf")

    val outputDF = readOutputJSON("token")

    val expectedDF = readExpectedMockJSON("expected_basic_authentication_token.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "authenticate request with basic authentication using username and password" in {
    runCheckpoint(checkpointFileName = "checkpoint_basic_authentication_up.conf")

    val outputDF = readOutputJSON("up")

    val expectedDF = readExpectedMockJSON("expected_basic_authentication_up.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
