package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ReplaceStringInColumnNameTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "replace the mentioned text from the specified column name from the restonomer response dataframe with the target text" in {
    runCheckpoint(checkpointFileName = "checkpoint_replace_string_in_column_name_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_replace_string_in_column_name_transformation.json")
  }

}
