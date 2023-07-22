package com.clairvoyant.restonomer.retry

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import com.clairvoyant.restonomer.core.exception.RestonomerException

class RestonomerRetryIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "retry"

  // StatusCode.OK

  it should "return the response body successfully in case of status 200" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_200.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_retry_status_200.json"))
  }

  // StatusCode.MovedPermanently (Redirection is done by akka itself)

  it should "return the response body successfully in case of status 301" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_301.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_retry_status_301.json"))
  }

  // StatusCode.Found (Redirection is done by restonomer)

  it should "return the response body successfully in case of status 302" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_302.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_retry_status_302.json"))
  }

  // StatusCode.TooManyRequests

  it should "return the response body successfully in case of status 429" in {
    runCheckpoint(checkpointFileName = "checkpoint_retry_status_429.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_retry_status_429.json"))
  }

  // StatusCode.NoContent

  it should "throw an exception in case of status 204" in {
    val thrown =
      the[RestonomerException] thrownBy runCheckpoint(checkpointFileName = "checkpoint_retry_status_204.conf")

    thrown.getMessage should equal("No Content.")
  }

  // Default Case

  it should "throw an exception in case of status 405" in {
    val thrown =
      the[RestonomerException] thrownBy runCheckpoint(checkpointFileName = "checkpoint_retry_status_405.conf")
    thrown.getMessage should equal("Something totally unexpected bad happened while calling the API 1 times.")
  }

}
