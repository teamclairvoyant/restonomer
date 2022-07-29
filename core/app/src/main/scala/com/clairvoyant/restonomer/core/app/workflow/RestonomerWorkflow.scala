package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.backend.RestonomerBackend
import com.clairvoyant.restonomer.core.http.request.RestonomerRequest
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig

object RestonomerWorkflow {
  def apply(restonomerBackend: RestonomerBackend): RestonomerWorkflow = new RestonomerWorkflow(restonomerBackend)
}

class RestonomerWorkflow(restonomerBackend: RestonomerBackend) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest: RestonomerRequest = RestonomerRequest(restonomerBackend, checkpointConfig.request)
    val restonomerResponse: RestonomerResponse = restonomerRequest.send()

    println(restonomerResponse)
  }

}
