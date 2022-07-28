package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.request.HttpRequest
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig

object RestonomerWorkflow {
  def apply(checkpointConfig: CheckpointConfig): RestonomerWorkflow = new RestonomerWorkflow(checkpointConfig)
}

class RestonomerWorkflow(checkpointConfig: CheckpointConfig) {

  def run(): Unit = {
    val response = HttpRequest(checkpointConfig.request)
      .build()
      .send(HttpBackendTypes(checkpointConfig.httpBackendType))

    println(response)
  }

}
