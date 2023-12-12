package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class FilterByRegexTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "filter records from restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_filter_regex_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_filter_regex_transformation.json")
  }

}
