package com.clairvoyant.restonomer.converter

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CSVResponseToDataFrameConverterIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "converter/csv"

  it should "convert the CSV response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_csv_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_csv_response.json"))
  }

}
