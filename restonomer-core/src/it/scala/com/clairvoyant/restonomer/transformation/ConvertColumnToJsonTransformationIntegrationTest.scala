package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class ConvertColumnToJsonTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "transform mentioned column from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_column_to_json_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_column_to_json_transformation.json"))
  }

}
