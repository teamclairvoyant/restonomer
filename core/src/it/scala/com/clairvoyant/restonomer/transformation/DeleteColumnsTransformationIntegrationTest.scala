package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class DeleteColumnsTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "delete mentioned columns from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_delete_columns_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_delete_columns_transformation.json"))
  }

}
