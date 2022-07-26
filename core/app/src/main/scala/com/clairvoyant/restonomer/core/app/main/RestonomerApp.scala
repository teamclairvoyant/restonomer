package com.clairvoyant.restonomer.core.app.main

import com.clairvoyant.restonomer.core.app.context.RestonomerContext

object RestonomerApp extends App {
  println("===== Welcome to Restonomer App =====")

  val restonomerContext = RestonomerContext()
  println(restonomerContext.configs)

  restonomerContext.runCheckpoint(checkpointName = "checkpoint_1")
}
