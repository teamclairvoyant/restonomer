package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class ReplaceNullsWithDefaultValueTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "replace nulls with default value as provided in the value map" in {
    runCheckpoint(checkpointFileName = "checkpoint_replace_nulls_with_default_value_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_replace_nulls_with_default_value_transformation.json")
  }

}
