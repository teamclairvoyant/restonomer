package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.http.request.enums.{HttpBackendTypes, HttpRequestTypes}
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig

object RestonomerWorkflow {
  def apply() = new RestonomerWorkflow
}

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val request = HttpRequestTypes(checkpointConfig.request).build()
    val response = request.send(HttpBackendTypes(checkpointConfig.httpBackendType))

    println(response)
  }

}
