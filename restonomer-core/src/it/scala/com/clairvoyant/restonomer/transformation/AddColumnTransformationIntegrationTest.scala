package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AddColumnTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform and add the column to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_column_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_add_column_transformation.json"))
  }

}
