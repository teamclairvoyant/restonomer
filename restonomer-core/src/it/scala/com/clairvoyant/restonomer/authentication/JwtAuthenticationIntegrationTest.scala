package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class JwtAuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/jwt_authentication"

  it should "authenticate request with subject and secret-key" in {
    runCheckpoint(checkpointFileName = "checkpoint_jwt_authentication.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_jwt_authentication.json"))
  }

}
