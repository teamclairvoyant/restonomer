package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class AddColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation/add_column"

  it should "transform and add the column to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_column_transformation.conf")

    val outputDF = readOutputJSON()

    val expectedDF = readExpectedMockJSON("expected_add_column_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
