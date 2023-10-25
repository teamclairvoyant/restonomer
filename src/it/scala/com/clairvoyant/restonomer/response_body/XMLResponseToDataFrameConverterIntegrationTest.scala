package com.clairvoyant.restonomer.response_body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class XMLResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "response_body"

  it should "convert the XML response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_xml_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame("expected_xml_response.json")
  }

}
