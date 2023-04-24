package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class TextDataBodyIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "body"

  it should "add custom text data body when provided by user" in {
    runCheckpoint(checkpointFileName = "checkpoint_text_data_request_body.conf")
    outputDF should matchExpectedDataFrame("expected_text_data_request_body.json")
  }

}
