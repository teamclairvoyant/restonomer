package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class ColumnCaseTransformationIntegrationTest extends IntegrationTestDependencies {
  override val mappingsDirectory: String = "transformation"

  it should "transform column case from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_column_case_transformation.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_column_case_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
