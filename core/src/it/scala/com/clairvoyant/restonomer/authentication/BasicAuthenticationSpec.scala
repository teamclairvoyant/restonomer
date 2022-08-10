package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class BasicAuthenticationSpec extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"

  it should "authenticate request with basic authentication using token" in {
    restonomerContext.runCheckpoint(checkpointName = "checkpoint_basic_authentication_token")
  }

  it should "authenticate request with basic authentication using username and password" in {
    restonomerContext.runCheckpoint(checkpointName = "checkpoint_basic_authentication_up")
  }

}
