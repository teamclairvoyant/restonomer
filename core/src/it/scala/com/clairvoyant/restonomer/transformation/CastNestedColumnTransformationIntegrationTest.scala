package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CastNestedColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "cast mentioned nested column as per the given ddl" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_nested_column_transformation.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_cast_nested_column_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
