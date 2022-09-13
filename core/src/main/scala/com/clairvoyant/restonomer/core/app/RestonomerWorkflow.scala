package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.http.RestonomerRequest
import com.clairvoyant.restonomer.core.model.CheckpointConfig

class RestonomerWorkflow {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    val restonomerRequest =
      RestonomerRequest
        .builder(checkpointConfig.request.method, checkpointConfig.request.url)
        .withAuthentication(checkpointConfig.request.authentication)
        .withHeaders(checkpointConfig.request.headers)
        .build

    val restonomerResponse = restonomerRequest.send(checkpointConfig.httpBackendType)

    val restonomerResponseBody =
      restonomerResponse.httpResponse.body match {
        case Left(errorMessage) =>
          throw new RestonomerException(errorMessage)
        case Right(responseBody) =>
          responseBody
      }

    println(restonomerResponseBody)

    val restonomerResponseDataFrame = ResponseToDataFrameConverter(checkpointConfig.response.body.format)
      .convertResponseToDataFrame(restonomerResponseBody)

    restonomerResponseDataFrame.show()
  }

}
