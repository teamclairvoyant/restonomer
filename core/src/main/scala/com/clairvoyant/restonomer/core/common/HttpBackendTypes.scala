package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend}

object HttpBackendTypes extends Enumeration {
  val HTTP_CLIENT_SYNC_BACKEND: HttpBackendTypes.Value = Value("HttpClientSyncBackend")

  def apply(httpBackendType: String): SttpBackend[Identity, Any] =
    if (isValidHttpBackend(httpBackendType))
      withName(httpBackendType) match {
        case HTTP_CLIENT_SYNC_BACKEND =>
          HttpClientSyncBackend()
      }
    else
      throw new RestonomerException(s"The http-backend-type: $httpBackendType is not supported.")

  private def isValidHttpBackend(httpBackendType: String): Boolean = values.exists(_.toString == httpBackendType)

}
