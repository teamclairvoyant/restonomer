package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ExplodeColumnTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "explodeColumn mentioned column from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_explode_column_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_explode_column_transformation.json"))
  }

}
