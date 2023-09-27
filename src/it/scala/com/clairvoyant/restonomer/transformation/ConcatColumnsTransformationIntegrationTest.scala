package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ConcatColumnsTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform and concat the columns to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_concat_columns_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_concat_columns_transformation.json")
  }

}
