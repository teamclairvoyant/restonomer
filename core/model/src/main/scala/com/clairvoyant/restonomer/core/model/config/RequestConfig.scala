package com.clairvoyant.restonomer.core.model.config

case class RequestConfig(
    name: String,
    requestType: String,
    method: String,
    url: String,
    authentication: Option[AuthenticationConfig]
)
