package com.clairvoyant.restonomer.query_params

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class QueryParamsIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "query_params"

  it should "add query params to the request url" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_query_params.conf")

    val outputDF = readOutputJSON()
    val expectedDF = readExpectedMockJSON(fileName = "expected_query_params.json")

    outputDF should matchExpectedDataFrame(expectedDF)
  }

}
