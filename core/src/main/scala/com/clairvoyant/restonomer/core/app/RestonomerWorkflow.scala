package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.CheckpointConfig

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val requestConfig = checkpointConfig.request

    val restonomerRequest =
      RestonomerRequest
        .builder(requestConfig.method, requestConfig.url)
        .withAuthentication(requestConfig.authentication)
        .withHeaders(requestConfig.headers)
        .build

    val restonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    println(restonomerResponse.httpResponse)
  }

}
