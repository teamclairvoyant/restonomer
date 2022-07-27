package com.clairvoyant.restonomer.core.http.request.enums

import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend}

import scala.language.implicitConversions

object HttpBackendTypes extends Enumeration {
  val HTTP_CLIENT_SYNC_BACKEND: HttpBackendTypes.Value = Value("HttpClientSyncBackend")

  def apply(httpBackendType: String): SttpBackend[Identity, Any] =
    if (isValidHttpBackend(httpBackendType))
      withName(httpBackendType) match {
        case HTTP_CLIENT_SYNC_BACKEND =>
          HttpClientSyncBackend()
      }
    else
      throw new IllegalArgumentException(s"The http-backend-type: $httpBackendType is not supported.")

  def isValidHttpBackend(httpBackendType: String): Boolean = values.exists(_.toString == httpBackendType)
}
