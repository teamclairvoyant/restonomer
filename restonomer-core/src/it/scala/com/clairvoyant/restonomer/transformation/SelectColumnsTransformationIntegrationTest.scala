package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class SelectColumnsTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "select the mentioned columns in the list from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_select_columns_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_select_columns_transformation.json"))
  }

}
