package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object MyTestApp extends App {

  private val restonomerContextDirectoryPath = "./my_restonomer_context"
  private val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)

  restonomerContext.runCheckpoint("checkpoint_no_authentication.conf")

}
