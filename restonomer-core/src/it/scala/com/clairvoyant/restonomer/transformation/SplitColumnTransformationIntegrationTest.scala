package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class SplitColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "split the column and create new columns accordingly" in {
    runCheckpoint(checkpointFileName = "checkpoint_split_column_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_split_column_transformation.json"))
  }

}
