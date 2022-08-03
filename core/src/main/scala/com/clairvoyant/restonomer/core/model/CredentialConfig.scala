package com.clairvoyant.restonomer.core.model

case class CredentialConfig(
    userName: Option[String],
    password: Option[String],
    basicToken: Option[String],
    bearerToken: Option[String]
)
