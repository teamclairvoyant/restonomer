package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.http.{RestonomerRequest, RestonomerResponse}
import com.clairvoyant.restonomer.core.model.CheckpointConfig

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest: RestonomerRequest = RestonomerRequest(checkpointConfig.request)
      .authenticate(checkpointConfig.authentication)

    val restonomerResponse: RestonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    println(restonomerResponse.httpResponse)
  }

}
