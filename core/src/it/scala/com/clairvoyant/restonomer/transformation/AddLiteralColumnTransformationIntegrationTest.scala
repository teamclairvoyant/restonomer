package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class AddLiteralColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "transform and add the column to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_literal_column_transformation.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_add_literal_column_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
