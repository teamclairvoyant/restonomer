package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BearerAuthenticationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "authentication/bearer_authentication"

  it should "authenticate request with bearer authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_bearer_authentication.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_bearer_authentication.json"))
  }

}
