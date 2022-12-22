package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import sttp.model.{Method, Uri}

case class RequestConfig(
    method: Method = Method.GET,
    url: Uri,
    queryParams: Map[String, String] = Map.empty,
    authentication: Option[RestonomerAuthentication] = None,
    headers: Map[String, String] = Map.empty,
    body: Option[String] = None,
    retry: RetryConfig = RetryConfig()
)
