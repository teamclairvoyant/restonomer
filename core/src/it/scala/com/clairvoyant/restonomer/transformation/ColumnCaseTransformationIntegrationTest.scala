package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class ColumnCaseTransformationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "transformation"

  it should "transform columns case from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_column_case_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_column_case_transformation.json"))
  }

}
