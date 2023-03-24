package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class BasicAuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/basic_authentication"

  it should "authenticate request with basic authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_basic_authentication_token.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_basic_authentication_token.json"))
  }

  it should "authenticate request with basic authentication using username and password" in {
    runCheckpoint(checkpointFileName = "checkpoint_basic_authentication_up.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_basic_authentication_up.json"))
  }

}
