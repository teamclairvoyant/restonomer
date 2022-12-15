package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CustomBodyIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "body"

  it should "providing raw text data as a body in request" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_custom_body.conf")

    val outputDF = readOutputJSON()

    val expectedDF = readExpectedMockJSON("expected_custom_body.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
