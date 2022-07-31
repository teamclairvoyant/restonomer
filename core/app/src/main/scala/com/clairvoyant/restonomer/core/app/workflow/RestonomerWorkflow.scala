package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.http.request.RestonomerRequest
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest: RestonomerRequest = RestonomerRequest(checkpointConfig.request)
      .authenticate(checkpointConfig.authentication)

    val restonomerResponse: RestonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    println(restonomerResponse.httpResponse)
  }

}
