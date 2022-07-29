package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.request.RestonomerRequest
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig

object RestonomerWorkflow {
  def apply(checkpointConfig: CheckpointConfig): RestonomerWorkflow = new RestonomerWorkflow(checkpointConfig)
}

class RestonomerWorkflow(checkpointConfig: CheckpointConfig) {

  def run(): Unit = {
    val request: RestonomerRequest = RestonomerRequest(
      requestConfig = checkpointConfig.request
    )

    val response = request.send(HttpBackendTypes(checkpointConfig.httpBackendType))

    println(response)
  }

}
