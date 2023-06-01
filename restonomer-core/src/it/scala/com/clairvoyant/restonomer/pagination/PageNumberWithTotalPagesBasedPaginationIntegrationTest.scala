package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class PageNumberWithTotalPagesBasedPaginationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_page_number_with_total_pages_based_pagination.conf")
    outputDF should matchExpectedDataFrame("expected_page_number_with_total_pages_based_pagination.json")
  }

}
