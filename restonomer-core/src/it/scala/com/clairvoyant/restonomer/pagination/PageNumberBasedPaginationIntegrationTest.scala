package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class PageNumberBasedPaginationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_page_number_based_pagination.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_page_number_based_pagination.json"))
  }

}
