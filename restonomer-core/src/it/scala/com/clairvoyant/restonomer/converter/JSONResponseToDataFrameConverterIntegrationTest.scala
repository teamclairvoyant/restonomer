package com.clairvoyant.restonomer.converter

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.*

class JSONResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "converter"

  it should "convert the JSON response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_json_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame("expected_json_response.json")
  }

}
