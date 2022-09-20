package com.clairvoyant.restonomer.converter

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class JSONResponseToDataFrameConverterIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "converter"

  it should "convert the JSON response body into a dataframe" in {
    restonomerContext.runCheckpoint(checkpointFilePath =
      s"$mappingsDirectory/checkpoint_json_response_dataframe_converter.conf"
    )
  }

}
