package com.clairvoyant.restonomer.core.model

case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    httpBackendType: Option[String],
    authentication: Option[AuthenticationConfig]
)
