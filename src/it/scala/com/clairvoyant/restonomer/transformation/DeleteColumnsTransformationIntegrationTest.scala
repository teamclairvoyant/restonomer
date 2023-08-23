package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class DeleteColumnsTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "delete mentioned columns from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_delete_columns_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_delete_columns_transformation.json")
  }

}
