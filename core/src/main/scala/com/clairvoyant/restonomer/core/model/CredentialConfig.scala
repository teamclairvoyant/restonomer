package com.clairvoyant.restonomer.core.model

case class CredentialConfig(
    userName: Option[String] = None,
    password: Option[String] = None,
    basicToken: Option[String] = None,
    bearerToken: Option[String] = None
)
