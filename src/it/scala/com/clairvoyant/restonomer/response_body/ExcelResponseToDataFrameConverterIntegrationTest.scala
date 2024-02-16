package com.clairvoyant.restonomer.response_body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ExcelResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "response_body"

  it should "convert the excel file response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_excel_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame("expected_excel_response.json")
  }

}
