package com.clairvoyant.restonomer.retry

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class RestonomerRetryIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "retry"

  // StatusCode.OK

  it should "return the response body successfully in case of status 200" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_200.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_200.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  // StatusCode.Found

  it should "return the response body successfully in case of status 302" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_302.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_302.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  // StatusCode.TooManyRequests

  it should "return the response body successfully in case of status 429" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_429.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_429.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
