package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class FilterRecordsTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation"

  it should "filter records from restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_filter_records_transformation.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_filter_records_transformation.json"))
  }

}
