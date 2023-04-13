package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class PageNumberBasedPaginationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_page_number_based_pagination.conf")
    outputDF should matchExpectedDataFrame("expected_page_number_based_pagination.json")
  }

}
