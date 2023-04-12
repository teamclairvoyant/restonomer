package com.clairvoyant.restonomer.authentication

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AwsSignatureAuthenticationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "authentication/aws_signature_authentication"

  it should "authenticate request with aws signature authentication using access and secrete key" in {
    runCheckpoint(checkpointFileName = "checkpoint_aws_signature_authentication.conf")

    outputDF.show(10,false)
    //outputDF should matchExpectedDataFrame(expectedDF("expected_aws_signature_authentication.json"))
  }

}
