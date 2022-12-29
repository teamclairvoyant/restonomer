package com.clairvoyant.restonomer.core.authentication

class TokenSubstitutor(token: String => String) {

  def substitute(credential: String): String =
    """token\[(.*)]""".r
      .findFirstMatchIn(credential)
      .map(matcher => token(matcher.group(1)))
      .getOrElse(credential)

}

object TokenSubstitutor {
  def apply(token: String => String): TokenSubstitutor = new TokenSubstitutor(token)
}
