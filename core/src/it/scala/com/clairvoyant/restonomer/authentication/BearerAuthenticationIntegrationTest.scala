package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BearerAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"

  it should "authenticate request with bearer authentication using token" in {
    restonomerContext.runCheckpoint("checkpoint_bearer_authentication_token.conf")
  }

}
