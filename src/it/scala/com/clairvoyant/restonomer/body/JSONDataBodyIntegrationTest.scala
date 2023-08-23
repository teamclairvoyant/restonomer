package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class JSONDataBodyIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "body"

  it should "add custom json data body when provided by user" in {
    runCheckpoint(checkpointFileName = "checkpoint_json_data_request_body.conf")
    outputDF should matchExpectedDataFrame("expected_json_data_request_body.json")
  }

}
