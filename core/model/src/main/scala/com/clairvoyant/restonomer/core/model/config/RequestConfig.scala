package com.clairvoyant.restonomer.core.model.config

case class RequestConfig(
    name: String,
    method: Option[String],
    url: String
)
