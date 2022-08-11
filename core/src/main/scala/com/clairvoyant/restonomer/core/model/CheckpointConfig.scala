package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication

case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    httpBackendType: Option[String],
    authentication: Option[RestonomerAuthentication]
)
