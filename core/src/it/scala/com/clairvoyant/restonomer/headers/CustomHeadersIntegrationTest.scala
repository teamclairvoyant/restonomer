package com.clairvoyant.restonomer.headers

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CustomHeadersIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "headers"

  it should "add no custom headers when empty headers provided" in {
    restonomerContext.runCheckpointWithPath("checkpoint_add_empty_custom_headers.conf")
  }

  it should "add no custom headers when headers are not provided" in {
    restonomerContext.runCheckpointWithPath("checkpoint_blank_custom_headers.conf")
  }

  it should "add custom headers when headers are provided" in {
    restonomerContext.runCheckpointWithPath("checkpoint_add_custom_headers.conf")
  }

}
