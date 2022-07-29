package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.common.enums.HttpRequestTypes._
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3._

abstract class RestonomerRequest(requestConfig: RequestConfig) {
  val restonomerAuthentication: Option[RestonomerAuthentication]
  val httpRequest: Request[Either[String, String], Any]

  def send(): Identity[Response[Either[String, String]]]
}

object RestonomerRequest {

  def apply(requestConfig: RequestConfig): RestonomerRequest = {
    if (isValidHttpRequestType(requestConfig.requestType)) {
      withName(requestConfig.requestType) match {
        case BASIC_REQUEST =>
          new BasicRequest(requestConfig)
        case BASIC_REQUEST_WITH_AUTHENTICATION =>
          new BasicRequestWithAuthentication(requestConfig)
      }
    } else
      throw new RestonomerContextException(s"The request-type: ${requestConfig.requestType} is not supported.")
  }

}
