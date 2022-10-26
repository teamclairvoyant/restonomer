package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.transformation.RestonomerTransformation

case class ResponseConfig(
    body: ResponseBodyConfig,
    transformations: Option[List[RestonomerTransformation]] = None
)
