package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.persistence.RestonomerPersistence
import com.clairvoyant.restonomer.core.transformation.RestonomerTransformation

case class ResponseConfig(
    retry: RetryConfig = RetryConfig(),
    body: ResponseBodyConfig,
    transformations: List[RestonomerTransformation] = List.empty,
    persistence: RestonomerPersistence
)

case class ResponseBodyConfig(format: String)
