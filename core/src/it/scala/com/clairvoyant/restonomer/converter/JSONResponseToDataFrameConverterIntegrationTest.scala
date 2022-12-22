package com.clairvoyant.restonomer.converter

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class JSONResponseToDataFrameConverterIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "converter/json"

  it should "convert the JSON response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_json_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_json_response.json"))
  }

}
