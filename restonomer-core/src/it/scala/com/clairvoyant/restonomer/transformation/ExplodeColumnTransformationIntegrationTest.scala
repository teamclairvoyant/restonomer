package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class ExplodeColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "explode mentioned column from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_explode_column_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_explode_column_transformation.json"))
  }

}
