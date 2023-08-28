package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ConvertArrayOfStructColumnsToArrayOfStringTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "convert all columns of array of struct type to array of string type" in {
    runCheckpoint(checkpointFileName = "checkpoint_convert_array_of_struct_to_array_of_string_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_convert_array_of_struct_to_array_of_string_transformation.json")
  }

}
