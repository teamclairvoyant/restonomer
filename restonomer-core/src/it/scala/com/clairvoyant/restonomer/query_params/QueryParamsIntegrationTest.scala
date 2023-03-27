package com.clairvoyant.restonomer.query_params

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class QueryParamsIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "query_params"

  it should "add query params to the request url" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_query_params.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_query_params.json"))
  }

  it should "add token in query params" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_token_in_query_params.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_test_token_for_query_params.json"))
  }

}
