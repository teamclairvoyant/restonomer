package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AddLiteralColumnTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform and add the column to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_literal_column_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_add_literal_column_transformation.json")
  }

}
