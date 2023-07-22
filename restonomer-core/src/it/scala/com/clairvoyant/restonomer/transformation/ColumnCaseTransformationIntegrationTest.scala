package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ColumnCaseTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {
  override val mappingsDirectory: String = "transformation"

  it should "transform columns case from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_column_case_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_column_case_transformation.json"))
  }

}
