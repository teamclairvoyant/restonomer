package com.clairvoyant.restonomer.retry

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class RestonomerRetryIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "retry"

  // StatusCode.OK

  it should "return the response body successfully in case of status 200" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_200_success.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_200_success.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  // StatusCode.Found

  it should "return the response body successfully in case of status 302" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_302_success.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_302_success.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
