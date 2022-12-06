package com.clairvoyant.restonomer.retry

import com.clairvoyant.restonomer.common.IntegrationTestDependencies
import com.clairvoyant.restonomer.core.exception.RestonomerException

class RestonomerRetryIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "retry"

  // StatusCode.OK

  it should "return the response body successfully in case of status 200" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_200.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_200.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  // StatusCode.MovedPermanently (Redirection is done by akka itself)

  it should "return the response body successfully in case of status 301" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_301.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_retry_status_301.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

  // StatusCode.Found (Redirection is done by restonomer)

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

  // StatusCode.NoContent

  it should "throw an exception in case of status 204" in {
    the[RestonomerException] thrownBy runCheckpoint(checkpointFileName =
      "checkpoint_retry_status_204.conf"
    ) should have message "No Content."
  }

  // Default Case

  it should "throw an exception in case of status 405" in {
    the[RestonomerException] thrownBy runCheckpoint(checkpointFileName =
      "checkpoint_retry_status_405.conf"
    ) should have message "Something totally unexpected bad happened while calling the API 1 times."
  }

}
