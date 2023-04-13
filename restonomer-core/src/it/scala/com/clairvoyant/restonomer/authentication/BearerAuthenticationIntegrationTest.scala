package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class BearerAuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/bearer_authentication"

  it should "authenticate request with bearer authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_bearer_authentication.conf")
    outputDF should matchExpectedDataFrame("expected_bearer_authentication.json")
  }

}
