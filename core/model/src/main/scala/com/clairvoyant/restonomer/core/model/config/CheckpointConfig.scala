package com.clairvoyant.restonomer.core.model.config

case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    httpBackendType: Option[String],
    authentication: Option[AuthenticationConfig]
)
