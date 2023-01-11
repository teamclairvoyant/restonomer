package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class ReplaceStringInColumnValueTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "replace mentioned columns values from the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_replace_StringValue_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_replace_StringValue_transformation.json"))
  }

}
