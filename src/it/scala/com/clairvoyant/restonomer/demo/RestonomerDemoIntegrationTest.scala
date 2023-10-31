package com.clairvoyant.restonomer.demo

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class RestonomerDemoIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "demo"

  it should "run the demo checkpoint successfully" in {
    runCheckpoint(checkpointFileName = "checkpoint_restonomer_demo.conf")
  }

}
