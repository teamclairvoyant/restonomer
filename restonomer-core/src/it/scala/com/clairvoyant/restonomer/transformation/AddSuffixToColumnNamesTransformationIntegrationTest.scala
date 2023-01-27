package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class AddSuffixToColumnNamesTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "add suffix to the mentioned column names from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_suffix_to_column_names_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_add_suffix_to_column_names_transformation.json"))
  }

}