package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class FlattenSchemaTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "flatten the schema of the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_flatten_schema_transformation.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_flatten_schema_transformation.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
