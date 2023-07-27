package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ChangeColumnCaseTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {
  override val mappingsDirectory: String = "transformation"

  it should "transform columns case from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_change_column_case_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_change_column_case_transformation.json")
  }

}
