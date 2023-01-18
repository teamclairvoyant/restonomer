package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class PaginationMechanism1IntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "pagination"

  it should "perform pagination and get the grouped responses" in {
    runCheckpoint(checkpointFileName = "checkpoint_pagination_mechanism_1.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_pagination_mechanism_1.json"))
  }

}
