package com.clairvoyant.restonomer.core.common

enum APIKeyPlaceholders {

  case QueryParam extends APIKeyPlaceholders
  case RequestHeader extends APIKeyPlaceholders
  case Cookie extends APIKeyPlaceholders

}

object APIKeyPlaceholders {

  def isValidAPIKeyPlaceholder(apiKeyPlaceholder: String): Boolean =
    APIKeyPlaceholders.values.exists(_.toString == apiKeyPlaceholder)

  def apply(placeholder: String): APIKeyPlaceholders = APIKeyPlaceholders.valueOf(placeholder)

}
