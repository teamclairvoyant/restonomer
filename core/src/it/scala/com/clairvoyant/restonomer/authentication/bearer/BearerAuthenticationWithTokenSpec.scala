package com.clairvoyant.restonomer.authentication.bearer

import com.clairvoyant.restonomer.IntegrationTestDependencies

class BearerAuthenticationWithTokenSpec extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "bearer_authentication"

  it should "authenticate request with bearer authentication using token" in {
    restonomerContext.runCheckpoint(checkpointName = "checkpoint_bearer_authentication_token")
  }

}
