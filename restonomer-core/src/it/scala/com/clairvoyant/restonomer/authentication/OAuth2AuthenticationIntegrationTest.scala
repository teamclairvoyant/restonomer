package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class OAuth2AuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/oauth2_authentication"

  it should "authenticate request with GrantType - ClientCredentials" in {
    runCheckpoint(checkpointFileName = "checkpoint_oauth2_authentication_client_credentials.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_oauth2_authentication_client_credentials.json"))
  }

}
