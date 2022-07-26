package com.clairvoyant.restonomer.core.app.main

import com.clairvoyant.restonomer.core.app.context.RestonomerContext
import com.clairvoyant.restonomer.core.app.workflow.RestonomerWorkflow

object RestonomerApp extends App {
  println("===== Welcome to Restonomer App =====")

  val restonomerContext = RestonomerContext()
  println(restonomerContext.configs)

  val restonomerWorkflow = new RestonomerWorkflow(restonomerContext)
  restonomerWorkflow.start(checkpointName = "checkpoint_1")
}
