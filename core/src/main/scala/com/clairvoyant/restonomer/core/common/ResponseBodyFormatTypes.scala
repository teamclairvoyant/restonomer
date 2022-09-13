package com.clairvoyant.restonomer.core.common

object ResponseBodyFormatTypes extends Enumeration {

  val JSON: ResponseBodyFormatTypes.Value = Value("JSON")

  def isValidResponseBodyFormat(responseBodyFormat: String): Boolean = values.exists(_.toString == responseBodyFormat)

  def apply(responseBodyFormat: String): ResponseBodyFormatTypes.Value =
    ResponseBodyFormatTypes.withName(responseBodyFormat)

}
