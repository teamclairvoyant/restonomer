package com.clairvoyant.restonomer.common

import cats.syntax.eq.*
import com.clairvoyant.restonomer.exception.RestonomerException

enum APIKeyPlaceholders {
  case QueryParam, RequestHeader, Cookie
}

object APIKeyPlaceholders {

  def apply(apiKeyPlaceholder: String): APIKeyPlaceholders =
    if (isValidAPIKeyPlaceholder(apiKeyPlaceholder))
      valueOf(apiKeyPlaceholder)
    else
      throw new RestonomerException(s"The API Key placeholder: $apiKeyPlaceholder is not supported.")

  def isValidAPIKeyPlaceholder(apiKeyPlaceholder: String): Boolean = values.exists(_.toString === apiKeyPlaceholder)

}
