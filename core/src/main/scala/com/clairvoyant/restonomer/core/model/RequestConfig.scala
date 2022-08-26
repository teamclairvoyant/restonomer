package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication

case class RequestConfig(
    method: Option[String],
    url: String,
    authentication: Option[RestonomerAuthentication] = None
)
