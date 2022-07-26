package com.clairvoyant.restonomer.core.app.workflow

import com.clairvoyant.restonomer.core.app.context.RestonomerContext
import com.clairvoyant.restonomer.core.model.config.{CheckpointConfig, RequestConfig}
import sttp.client3.{basicRequest, HttpClientSyncBackend, UriContext}
import sttp.model.Method

class RestonomerWorkflow(restonomerContext: RestonomerContext) {

  def run(checkpointName: String): Unit = {
    val checkpointConfig: Option[CheckpointConfig] = restonomerContext.configs.checkpoints
      .find(_.name == checkpointName)

    val requestConfig: Option[RequestConfig] = checkpointConfig.flatMap(_.request)

    requestConfig.foreach { requestConfig =>
      val protocol =
        if (requestConfig.isHttps)
          "https://"
        else
          "http://"

      val finalURL = uri"$protocol${requestConfig.domain}${requestConfig.url}"

      val response = basicRequest
        .method(Method.apply(requestConfig.method), finalURL)
        .send(HttpClientSyncBackend())

      println(response.body)
    }

  }

}
