package com.clairvoyant.restonomer.core.model

case class ResponseConfig(
    body: ResponseBodyConfig,
    transformations: Option[List[RestonomerTransformation]]
)
