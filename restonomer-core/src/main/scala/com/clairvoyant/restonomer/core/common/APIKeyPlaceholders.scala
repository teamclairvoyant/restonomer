package com.clairvoyant.restonomer.core.common

object APIKeyPlaceholders extends Enumeration {
  val QUERY_PARAM: APIKeyPlaceholders.Value = Value("QueryParam")
  val REQUEST_HEADER: APIKeyPlaceholders.Value = Value("RequestHeader")
  val COOKIE: APIKeyPlaceholders.Value = Value("Cookie")

  def isValidAPIKeyPlaceholder(apiKeyPlaceholder: String): Boolean = values.exists(_.toString == apiKeyPlaceholder)

  def apply(placeholder: String): APIKeyPlaceholders.Value = APIKeyPlaceholders.withName(placeholder)
}
