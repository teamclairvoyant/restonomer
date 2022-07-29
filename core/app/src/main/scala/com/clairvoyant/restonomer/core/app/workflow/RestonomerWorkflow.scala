package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.http.request.RestonomerRequest
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import sttp.client3.{Identity, Response}

object RestonomerWorkflow {
  def apply(checkpointConfig: CheckpointConfig): RestonomerWorkflow = new RestonomerWorkflow(checkpointConfig)
}

class RestonomerWorkflow(checkpointConfig: CheckpointConfig) {

  def run(): Unit = {
    val restonomerRequest: RestonomerRequest = RestonomerRequest(checkpointConfig.request)
    val restonomerResponse: Identity[Response[Either[String, String]]] = restonomerRequest.send()

    println(restonomerResponse)
  }

}
