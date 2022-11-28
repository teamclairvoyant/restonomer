package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.{HttpClientFutureBackend, SttpBackend}

import scala.concurrent.Future

object HttpBackendTypes extends Enumeration {
  val HTTP_CLIENT_FUTURE_BACKEND: HttpBackendTypes.Value = Value("HttpClientSyncBackend")

  def apply(httpBackendType: String): SttpBackend[Future, _] =
    if (isValidHttpBackend(httpBackendType))
      withName(httpBackendType) match {
        case HTTP_CLIENT_FUTURE_BACKEND =>
          HttpClientFutureBackend()
      }
    else
      throw new RestonomerException(s"The http-backend-type: $httpBackendType is not supported.")

  private def isValidHttpBackend(httpBackendType: String): Boolean = values.exists(_.toString == httpBackendType)

}
