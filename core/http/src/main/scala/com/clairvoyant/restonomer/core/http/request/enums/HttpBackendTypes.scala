package com.clairvoyant.restonomer.core.http.request.enums

import scala.language.implicitConversions

object HttpBackendTypes extends Enumeration {
  val HTTP_CLIENT_SYNC_BACKEND: HttpBackendTypes.Value = Value("HttpClientSyncBackend")

  def apply(httpBackendType: String): HttpBackendTypes.Value =
    if (isValidHttpBackend(httpBackendType))
      withName(httpBackendType)
    else
      throw new IllegalArgumentException(s"The httpBackend: $httpBackendType is not supported.")

  def isValidHttpBackend(httpBackendType: String): Boolean = values.exists(_.toString == httpBackendType)
}
