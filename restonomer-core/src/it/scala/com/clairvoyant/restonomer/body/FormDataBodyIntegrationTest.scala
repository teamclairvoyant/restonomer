package com.clairvoyant.restonomer.body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class FormDataBodyIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "body"

  it should "add custom form data body when provided by user" in {
    runCheckpoint(checkpointFileName = "checkpoint_form_data_request_body.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_form_data_request_body.json"))
  }

}
