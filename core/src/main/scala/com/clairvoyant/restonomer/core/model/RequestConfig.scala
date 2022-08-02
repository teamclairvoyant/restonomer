package com.clairvoyant.restonomer.core.model

case class RequestConfig(
    name: String,
    method: Option[String],
    url: String
)
