package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class SplitColumnTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "split the column and create new columns accordingly" in {
    runCheckpoint(checkpointFileName = "checkpoint_split_column_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_split_column_transformation.json"))
  }

}
