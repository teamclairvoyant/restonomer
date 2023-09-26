package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CoalesceColumnsTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform and coalesce the columns to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_coalesce_columns_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_coalesce_columns_transformation.json")
  }

}
