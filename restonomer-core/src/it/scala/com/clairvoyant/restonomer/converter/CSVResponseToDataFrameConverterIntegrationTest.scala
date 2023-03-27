package com.clairvoyant.restonomer.converter

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CSVResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "converter"

  it should "convert the CSV response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_csv_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_csv_response.json"))
  }

}
