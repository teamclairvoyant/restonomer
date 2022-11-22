package com.clairvoyant.restonomer.core.model

case class TokenConfig(
    tokenRequest: RequestConfig,
    tokenResponse: TokenResponseConfig
)

case class TokenResponseConfig(placeholder: String)
