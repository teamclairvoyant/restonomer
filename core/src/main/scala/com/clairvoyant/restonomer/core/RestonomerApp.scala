package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object RestonomerApp extends App {
  private val configVariables = Map("BASIC_AUTH_TOKEN" -> "cG9zdG1hbjpwYXNzd29yZA==")

  private val restonomerContext = RestonomerContext(configVariablesFromApplicationArgs = configVariables)

  //restonomerContext.runCheckpoint(checkpointFilePath = "checkpoint_no_authentication.conf")
  restonomerContext.runCheckpointsUnderDirectory(checkpointsDirectoryPath = "category-1")
  //restonomerContext.runAllCheckpoints()
}
