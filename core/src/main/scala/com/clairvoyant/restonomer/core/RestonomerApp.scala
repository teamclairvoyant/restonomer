package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object RestonomerApp extends App {

  private val restonomerContext = RestonomerContext()

  restonomerContext.runCheckpoint(checkpointFilePath = "checkpoint_no_authentication.conf")
  // restonomerContext.runCheckpointsUnderDirectory(checkpointsDirectoryPath = "category-1")
  // restonomerContext.runAllCheckpoints()
}
