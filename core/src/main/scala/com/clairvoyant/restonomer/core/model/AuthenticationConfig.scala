package com.clairvoyant.restonomer.core.model

case class AuthenticationConfig(
    name: String,
    authenticationType: String,
    credentials: CredentialConfig
)
