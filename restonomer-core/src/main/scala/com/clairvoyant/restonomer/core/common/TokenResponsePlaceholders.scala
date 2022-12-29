package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException

object TokenResponsePlaceholders extends Enumeration {

  val RESPONSE_BODY: TokenResponsePlaceholders.Value = Value("ResponseBody")
  val RESPONSE_HEADERS: TokenResponsePlaceholders.Value = Value("ResponseHeaders")

  def apply(tokenResponsePlaceholder: String): TokenResponsePlaceholders.Value = {
    if (isValidTokenResponsePlaceholder(tokenResponsePlaceholder))
      TokenResponsePlaceholders.withName(tokenResponsePlaceholder)
    else
      throw new RestonomerException(s"The token response placeholder: $tokenResponsePlaceholder is not supported.")
  }

  private def isValidTokenResponsePlaceholder(tokenResponsePlaceholder: String): Boolean =
    values.exists(_.toString == tokenResponsePlaceholder)

}
