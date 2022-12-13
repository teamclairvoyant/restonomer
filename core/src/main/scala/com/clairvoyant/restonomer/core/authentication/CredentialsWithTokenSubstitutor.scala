package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exception.RestonomerException

class CredentialsWithTokenSubstitutor(tokens: Map[String, String]) {

  def substituteCredentialWithToken(credential: String): String =
    """token\[(.*)]""".r
      .findFirstMatchIn(credential)
      .map { matcher =>
        tokens.get(matcher.group(1)) match {
          case Some(value) =>
            value
          case None =>
            throw new RestonomerException(
              s"Could not find the value of $credential in the token response: $tokens"
            )
        }
      }
      .getOrElse(credential)

}

object CredentialsWithTokenSubstitutor {
  def apply(tokens: Map[String, String]): CredentialsWithTokenSubstitutor = new CredentialsWithTokenSubstitutor(tokens)
}
