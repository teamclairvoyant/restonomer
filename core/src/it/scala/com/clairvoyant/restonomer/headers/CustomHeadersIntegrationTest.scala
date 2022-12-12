package com.clairvoyant.restonomer.headers

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CustomHeadersIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "headers"

  it should "add no custom headers when empty headers provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_empty_custom_headers.conf")

    val outputDF = readOutputJSON("empty_custom_headers")

    val expectedDF = readExpectedMockJSON("expected_empty_custom_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "add no custom headers when headers are not provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_blank_custom_headers.conf")

    val outputDF = readOutputJSON("blank_custom_headers")

    val expectedDF = readExpectedMockJSON("expected_custom_body.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  it should "add custom headers when headers are provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_headers.conf")

    val outputDF = readOutputJSON("custom_headers")

    val expectedDF = readExpectedMockJSON("expected_custom_headers.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
