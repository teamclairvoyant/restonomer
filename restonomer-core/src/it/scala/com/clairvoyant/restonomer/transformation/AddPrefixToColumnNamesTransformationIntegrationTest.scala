package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AddPrefixToColumnNamesTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "add prefix to select/all column names of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_prefix_to_column_names_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_add_prefix_to_column_names_transformation.json")
  }

}
