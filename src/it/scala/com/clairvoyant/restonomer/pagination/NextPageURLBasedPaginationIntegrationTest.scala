package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class NextPageURLBasedPaginationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_next_page_url_based_pagination.conf")
    outputDF should matchExpectedDataFrame("expected_next_page_url_based_pagination.json")
  }

}
