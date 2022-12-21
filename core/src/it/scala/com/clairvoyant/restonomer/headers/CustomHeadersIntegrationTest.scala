package com.clairvoyant.restonomer.headers

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CustomHeadersIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "headers"

  it should "add no custom headers when empty headers provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_empty_custom_headers.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_empty_custom_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "add no custom headers when headers are not provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_blank_custom_headers.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_blank_custom_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "add custom headers when headers are provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_headers.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_custom_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
