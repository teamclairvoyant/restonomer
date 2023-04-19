package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CustomBodyIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "body"

  it should "add custom body when provided by user" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_body.conf")
    outputDF should matchExpectedDataFrame("expected_custom_body.json")
  }

}
