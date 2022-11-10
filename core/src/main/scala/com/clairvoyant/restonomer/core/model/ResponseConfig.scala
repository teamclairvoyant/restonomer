package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.persistence.RestonomerPersistence
import com.clairvoyant.restonomer.core.transformation.RestonomerTransformation

case class ResponseConfig(
    body: ResponseBodyConfig,
    transformations: List[RestonomerTransformation] = List.empty,
    persistence: RestonomerPersistence
)
