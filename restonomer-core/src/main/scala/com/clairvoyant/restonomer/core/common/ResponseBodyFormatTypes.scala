package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException

object ResponseBodyFormatTypes extends Enumeration {

  val JSON: ResponseBodyFormatTypes.Value = Value("JSON")
  val CSV: ResponseBodyFormatTypes.Value = Value("CSV")

  def apply(responseBodyFormat: String): ResponseBodyFormatTypes.Value = {
    if (isValidResponseBodyFormat(responseBodyFormat))
      ResponseBodyFormatTypes.withName(responseBodyFormat)
    else
      throw new RestonomerException(s"The response body format: $responseBodyFormat is not supported.")
  }

  private def isValidResponseBodyFormat(responseBodyFormat: String): Boolean =
    values.exists(_.toString == responseBodyFormat)

}
