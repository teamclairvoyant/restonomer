package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.http.request.RestonomerRequest
import com.clairvoyant.restonomer.core.http.response.RestonomerResponse
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import sttp.client3.{UriContext, basicRequest}
import sttp.model.Method

object RestonomerWorkflow {
  def apply(): RestonomerWorkflow = new RestonomerWorkflow()
}

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest: RestonomerRequest = RestonomerRequest(
      basicRequest.method(
        method = checkpointConfig.request.method.map(Method(_)).getOrElse(Method.GET),
        uri = uri"${checkpointConfig.request.url}"
      )
    ).authenticate(checkpointConfig.request.authentication)

    val restonomerResponse: RestonomerResponse = restonomerRequest.send(checkpointConfig.request.httpBackendType)

    println(restonomerResponse.httpResponse)
  }

}
