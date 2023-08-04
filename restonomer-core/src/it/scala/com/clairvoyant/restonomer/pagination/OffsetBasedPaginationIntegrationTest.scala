package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class OffsetBasedPaginationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_offset_based_pagination.conf")
    outputDF should matchExpectedDataFrame("expected_offset_based_pagination.json")
  }

}
