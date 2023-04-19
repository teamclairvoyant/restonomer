package com.clairvoyant.restonomer.headers

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CustomHeadersIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "headers"

  it should "add no custom headers when empty headers provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_empty_custom_headers.conf")
    outputDF should matchExpectedDataFrame("expected_empty_custom_headers.json")
  }

  it should "add no custom headers when headers are not provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_blank_custom_headers.conf")
    outputDF should matchExpectedDataFrame("expected_blank_custom_headers.json")
  }

  it should "add custom headers when headers are provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_headers.conf")
    outputDF should matchExpectedDataFrame("expected_custom_headers.json")
  }

}
