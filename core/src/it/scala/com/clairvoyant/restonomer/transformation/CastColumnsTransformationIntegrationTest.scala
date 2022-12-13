package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class CastColumnsTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_transformation.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_cast_columns_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
