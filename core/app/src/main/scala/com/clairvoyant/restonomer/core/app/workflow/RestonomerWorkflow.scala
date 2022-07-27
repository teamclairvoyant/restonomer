package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.http.request.builder.HttpRequestBuilder
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import sttp.client3.HttpClientSyncBackend

object RestonomerWorkflow {
  def apply() = new RestonomerWorkflow
}

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val request = HttpRequestBuilder(checkpointConfig.request).buildHttpRequest
    val response = request.send(HttpClientSyncBackend())

    println(response)
  }

}
