package com.clairvoyant.restonomer.common

import cats.syntax.eq.*
import com.clairvoyant.restonomer.exception.RestonomerException
import sttp.client3.*

enum ResponseBodyCompressionTypes:
  case GZIP

object ResponseBodyCompressionTypes {

  def apply(responseBodyCompressionType: String): ResponseBodyCompressionTypes =
    if (isValidResponseBodyCompressionType(responseBodyCompressionType))
      valueOf(responseBodyCompressionType)
    else
      throw new RestonomerException(
        s"The response body compression type: $responseBodyCompressionType is not supported."
      )

  private def isValidResponseBodyCompressionType(responseBodyCompressionType: String): Boolean =
    values.exists(_.toString === responseBodyCompressionType)

}
