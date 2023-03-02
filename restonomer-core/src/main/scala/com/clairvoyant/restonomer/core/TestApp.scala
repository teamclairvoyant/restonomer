package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object TestApp extends App {

  private val restonomerContextDirectoryPath = "./my_restonomer_context"
  private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

  restonomerContext.runAllCheckpoints()
}
