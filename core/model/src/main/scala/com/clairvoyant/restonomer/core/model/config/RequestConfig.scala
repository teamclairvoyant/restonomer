package com.clairvoyant.restonomer.core.model.config

case class RequestConfig(
    name: String,
    method: String,
    isHttps: Boolean,
    domain: String,
    url: String,
    authentication: Option[AuthenticationConfig]
)
