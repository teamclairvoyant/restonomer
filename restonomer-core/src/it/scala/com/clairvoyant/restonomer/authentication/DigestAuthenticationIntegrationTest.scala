package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class DigestAuthenticationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "authentication/digest_authentication"

  it should "authenticate request with digest authentication using token" in {
    runCheckpoint(checkpointFileName = "checkpoint_digest_authentication.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_digest_authentication.json"))
  }

}