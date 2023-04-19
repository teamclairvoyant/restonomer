package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AddSuffixToColumnNamesTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "add suffix to the mentioned column names from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_suffix_to_column_names_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_add_suffix_to_column_names_transformation.json")
  }

}
