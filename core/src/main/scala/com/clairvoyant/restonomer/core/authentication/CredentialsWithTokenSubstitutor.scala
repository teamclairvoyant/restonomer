package com.clairvoyant.restonomer.core.authentication

class CredentialsWithTokenSubstitutor(token: String => String) {

  def substituteCredentialWithToken(credential: String): String =
    """token\[(.*)]""".r
      .findFirstMatchIn(credential)
      .map(matcher => token(matcher.group(1)))
      .getOrElse(credential)

}

object CredentialsWithTokenSubstitutor {
  def apply(token: String => String): CredentialsWithTokenSubstitutor = new CredentialsWithTokenSubstitutor(token)
}
