package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication

case class RequestConfig(
    method: String = "GET",
    url: String,
    queryParams: Map[String, String] = Map.empty,
    authentication: Option[RestonomerAuthentication] = None,
    headers: Map[String, String] = Map.empty,
    body: Option[String] = None,
    retry: RetryConfig = RetryConfig()
)
