package com.clairvoyant.restonomer.response_body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class HTMLTableResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "response_body"

  it should "convert the HTML response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_html_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame("expected_html_response.json")
  }

}
