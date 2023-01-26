package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class RenameColumnsTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "rename the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_rename_columns_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_rename_columns_transformation.json"))
  }

}
