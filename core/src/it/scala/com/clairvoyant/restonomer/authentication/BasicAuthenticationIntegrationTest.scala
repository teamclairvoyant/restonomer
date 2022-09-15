package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BasicAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"

  it should "authenticate request with basic authentication using token" in {
    restonomerContext.runCheckpoint("checkpoint_basic_authentication_token.conf")
  }

  it should "authenticate request with basic authentication using username and password" in {
    restonomerContext.runCheckpoint("checkpoint_basic_authentication_up.conf")
  }

}
