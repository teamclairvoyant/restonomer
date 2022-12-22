package com.clairvoyant.restonomer.persistence

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class FileSystemPersistenceIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "persistence"

  it should "persist the restonomer response dataframe in the file system in the desired format at the desired path" in {
    runCheckpoint(checkpointFileName = "checkpoint_file_system_persistence.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_file_system_persistence.json"))
  }

}
