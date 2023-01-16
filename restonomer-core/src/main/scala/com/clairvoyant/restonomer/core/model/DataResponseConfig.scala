package com.clairvoyant.restonomer.core.model

import com.clairvoyant.restonomer.core.pagination.RestonomerPagination
import com.clairvoyant.restonomer.core.persistence.RestonomerPersistence
import com.clairvoyant.restonomer.core.transformation.RestonomerTransformation

case class DataResponseConfig(
    bodyFormat: String,
    pagination: Option[RestonomerPagination] = None,
    transformations: List[RestonomerTransformation] = List.empty,
    persistence: RestonomerPersistence
)
