package com.clairvoyant.restonomer.core.app.request

import com.clairvoyant.restonomer.core.common.enums.HttpBackendTypes
import com.clairvoyant.restonomer.core.http.request.HttpRequest
import com.clairvoyant.restonomer.core.model.config.RequestConfig
import sttp.client3.{Identity, Response}

class RestonomerRequest(httpRequest: Option[HttpRequest]) {

  def send(httpBackendType: String): Option[Identity[Response[Either[String, String]]]] =
    httpRequest
      .map(_.build())
      .map(_.send(HttpBackendTypes(httpBackendType)))

}

object RestonomerRequest {
  def builder: RestonomerRequestBuilder = RestonomerRequestBuilder()
}

case class RestonomerRequestBuilder(httpRequest: Option[HttpRequest] = None) {

  def withRequestConfig(requestConfig: RequestConfig): RestonomerRequestBuilder =
    this.copy(httpRequest = Option(HttpRequest(requestConfig)))

  def build: RestonomerRequest = new RestonomerRequest(httpRequest)

}
