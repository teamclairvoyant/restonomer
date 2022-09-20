package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class APIKeyAuthenticationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "authentication"

  it should "authenticate request with api key authentication using query string" in {
    restonomerContext.runCheckpointWithPath("checkpoint_api_key_authentication_query_string.conf")
  }

  it should "authenticate request with api key authentication using request header" in {
    restonomerContext.runCheckpointWithPath("checkpoint_api_key_authentication_request_header.conf")
  }

  it should "authenticate request with api key authentication using cookie" in {
    restonomerContext.runCheckpointWithPath("checkpoint_api_key_authentication_cookie.conf")
  }

}