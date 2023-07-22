package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class CursorBasedPaginationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_cursor_based_pagination.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_cursor_based_pagination.json"))
  }

}
