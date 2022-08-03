package com.clairvoyant.restonomer.core.model

case class AuthenticationConfig(
    authenticationType: String,
    credentials: CredentialConfig
)
