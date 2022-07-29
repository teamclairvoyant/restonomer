package com.clairvoyant.restonomer.core.model.config

case class AuthenticationConfig(
    name: String,
    authenticationType: String,
    credentials: CredentialConfig
)