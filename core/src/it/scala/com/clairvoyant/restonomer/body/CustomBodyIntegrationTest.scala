package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CustomBodyIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "body"

  it should "add custom body when provided by user" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_body.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_custom_body.json"))
  }

}
