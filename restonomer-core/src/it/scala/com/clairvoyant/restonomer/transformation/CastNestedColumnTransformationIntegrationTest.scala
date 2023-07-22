package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CastNestedColumnTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "cast mentioned nested column as per the given ddl" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_nested_column_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_cast_nested_column_transformation.json"))
  }

}
