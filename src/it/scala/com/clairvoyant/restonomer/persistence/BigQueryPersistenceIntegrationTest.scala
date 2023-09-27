package com.clairvoyant.restonomer.persistence

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class BigQueryPersistenceIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "persistence"

  it should "persist the restonomer response dataframe in the BQ table" in {
    runCheckpoint(checkpointFileName = "checkpoint_big_query_persistence.conf")
    // readJSONFromFile(outputPath) should matchExpectedDataFrame("expected_local_file_system_persistence.json")
  }

}
