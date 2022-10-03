package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.common.HttpBackendTypes

case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    response: ResponseConfig,
    httpBackendType: String = HttpBackendTypes.HTTP_CLIENT_SYNC_BACKEND.toString
)
