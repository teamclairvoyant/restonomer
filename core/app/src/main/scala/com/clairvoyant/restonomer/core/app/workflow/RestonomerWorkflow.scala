package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.app.context.RestonomerContext
import com.clairvoyant.restonomer.core.http.HttpRequestBuilder
import sttp.client3.HttpClientSyncBackend

object RestonomerWorkflow {
  def apply(restonomerContext: RestonomerContext) = new RestonomerWorkflow(restonomerContext)
}

class RestonomerWorkflow(restonomerContext: RestonomerContext) {

  def run(checkpointName: String): Unit = {
    restonomerContext.configs.checkpoints
      .find(_.name == checkpointName)
      .map(_.request)
      .foreach { requestConfig =>
        val request = HttpRequestBuilder(requestConfig).buildHttpRequest
        val response = request.send(HttpClientSyncBackend())

        println(response.body)
      }
  }

}
