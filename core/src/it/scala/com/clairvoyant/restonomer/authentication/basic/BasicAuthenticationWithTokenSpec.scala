package com.clairvoyant.restonomer.authentication.basic

import com.clairvoyant.restonomer.IntegrationTestDependencies

class BasicAuthenticationWithTokenSpec extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "basic_authentication"

  it should "authenticate request with basic authentication using token" in {
    restonomerContext.runCheckpoint("checkpoint_basic_authentication_token")
  }

}
