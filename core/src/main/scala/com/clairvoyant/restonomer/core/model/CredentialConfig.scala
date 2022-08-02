package com.clairvoyant.restonomer.core.model

case class CredentialConfig(
    userName: Option[String],
    password: Option[String],
    token: Option[String]
)
