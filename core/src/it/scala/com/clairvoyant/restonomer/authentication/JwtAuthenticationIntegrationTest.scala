package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class JwtAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"
  val checkpointsDirectoryPath = s"$mappingsDirectory/jwt_authentication"

  it should "authenticate request with subject and secret-key" in {
    restonomerContext.runCheckpoint(checkpointFilePath =
      s"$checkpointsDirectoryPath/checkpoint_jwt_authentication.conf"
    )
  }

}
