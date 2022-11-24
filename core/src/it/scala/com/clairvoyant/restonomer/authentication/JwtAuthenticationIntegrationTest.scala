package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class JwtAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication/jwt_authentication"

  it should "authenticate request with subject and secret-key" in {
    runCheckpoint(checkpointFileName = s"checkpoint_jwt_authentication.conf")
  }

}
