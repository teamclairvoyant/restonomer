package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class JwtAuthenticationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "authentication/jwt_authentication"

  it should "authenticate request with subject and secret-key" in {
    runCheckpoint(checkpointFileName = "checkpoint_jwt_authentication.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_jwt_authentication.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
