package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CastColumnsBasedOnSuffixTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_based_on_suffix_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_cast_columns_based_on_suffix_transformation.json"))
  }

}
