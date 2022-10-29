package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import sttp.model.Method

case class RequestConfig(
    method: String = Method.GET.method,
    url: String,
    authentication: Option[RestonomerAuthentication] = None,
    headers: Map[String, String] = Map[String, String]().empty
    
    // Declaring body as optional - Rest 51
    body: Option[String] = None
)
