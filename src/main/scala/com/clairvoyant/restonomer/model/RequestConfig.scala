package com.clairvoyant.restonomer.model

import com.clairvoyant.restonomer.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.body.RestonomerRequestBody

case class RequestConfig(
    method: String = "GET",
    url: String,
    queryParams: Map[String, String] = Map.empty,
    authentication: Option[RestonomerAuthentication] = None,
    headers: Map[String, String] = Map.empty,
    body: Option[RestonomerRequestBody] = None,
    retry: RetryConfig = RetryConfig()
)
