package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.app.context.RestonomerContext

class RestonomerWorkflow(restonomerContext: RestonomerContext) {

  def run(checkpointName: String): Unit = {
    println(s"checkpointName ==> $checkpointName")
  }

}
