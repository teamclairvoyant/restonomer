package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BearerAuthenticationSpec extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"

  it should "authenticate request with bearer authentication using token" in {
    restonomerContext.runCheckpoint(checkpointName = "checkpoint_bearer_authentication_token")
  }

}
