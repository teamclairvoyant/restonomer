package com.clairvoyant.restonomer.authentication

class TokenSubstitutor(token: String => String) {

  def substitute(credential: String): String =
    """token\[(.*)]""".r
      .findFirstMatchIn(credential)
      .map(matcher => token(matcher.group(1)))
      .getOrElse(credential)

}
