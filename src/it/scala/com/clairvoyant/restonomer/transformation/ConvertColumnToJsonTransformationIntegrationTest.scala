package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ConvertColumnToJsonTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform mentioned column from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_column_to_json_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_column_to_json_transformation.json")
  }

}
