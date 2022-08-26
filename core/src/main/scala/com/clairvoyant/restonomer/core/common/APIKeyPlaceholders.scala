package com.clairvoyant.restonomer.core.common

object APIKeyPlaceholders extends Enumeration {
  val QUERY_STRING: APIKeyPlaceholders.Value = Value("QueryString")
  val REQUEST_HEADER: APIKeyPlaceholders.Value = Value("RequestHeader")
  val COOKIE: APIKeyPlaceholders.Value = Value("Cookie")

  def isValidAPIKeyPlaceholder(apiKeyPlaceholder: String): Boolean = values.exists(_.toString == apiKeyPlaceholder)

  def apply(placeholder: String): APIKeyPlaceholders.Value = APIKeyPlaceholders.withName(placeholder)
}
