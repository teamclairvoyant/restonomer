package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ConvertJSONStringToStructTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "convert json string column to struct type" in {
    runCheckpoint(checkpointFileName = "checkpoint_convert_json_string_to_struct_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_convert_json_string_to_struct_transformation.json")
  }

}
