package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class SelectColumnsTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "select columns as per provided expressions" in {
    runCheckpoint(checkpointFileName = "checkpoint_select_columns_with_expressions_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_select_columns_with_expressions_transformation.json")
  }

}
