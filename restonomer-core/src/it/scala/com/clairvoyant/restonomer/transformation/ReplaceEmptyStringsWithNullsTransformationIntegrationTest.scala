package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies
import com.clairvoyant.restonomer.common.MockFileSystemPersistence

class ReplaceEmptyStringsWithNullsTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "replace empty strings with nulls in restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_replace_empty_strings_with_nulls_transformation.conf")
    outputDF should matchExpectedDataFrame(
      readMockJSON("expected_replace_empty_strings_with_nulls_transformation.json")
    )
  }

}
