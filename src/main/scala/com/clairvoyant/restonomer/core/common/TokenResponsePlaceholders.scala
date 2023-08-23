package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException

enum TokenResponsePlaceholders {
  case ResponseBody, ResponseHeaders
}

object TokenResponsePlaceholders {

  def apply(tokenResponsePlaceholder: String): TokenResponsePlaceholders = {
    if (isValidTokenResponsePlaceholder(tokenResponsePlaceholder))
      valueOf(tokenResponsePlaceholder)
    else
      throw new RestonomerException(s"The token response placeholder: $tokenResponsePlaceholder is not supported.")
  }

  private def isValidTokenResponsePlaceholder(tokenResponsePlaceholder: String): Boolean =
    values.exists(_.toString == tokenResponsePlaceholder)

}
