package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.app.request.RestonomerRequest
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import sttp.client3.{Identity, Response}

object RestonomerWorkflow {
  def apply(checkpointConfig: CheckpointConfig): RestonomerWorkflow = new RestonomerWorkflow(checkpointConfig)
}

class RestonomerWorkflow(checkpointConfig: CheckpointConfig) {

  def run(): Unit = {
    val restonomerRequest: RestonomerRequest =
      RestonomerRequest.builder
        .withRequestConfig(checkpointConfig.request)
        .build

    val response: Option[Identity[Response[Either[String, String]]]] = restonomerRequest.send(
      checkpointConfig.httpBackendType
    )

    println(response)
  }

}
