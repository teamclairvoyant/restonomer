package com.clairvoyant.restonomer.model

import com.clairvoyant.restonomer.pagination.RestonomerPagination
import com.clairvoyant.restonomer.persistence.RestonomerPersistence
import com.clairvoyant.restonomer.transformation.RestonomerTransformation

case class DataResponseConfig(
    body: DataResponseBodyConfig,
    pagination: Option[RestonomerPagination] = None,
    transformations: List[RestonomerTransformation] = List.empty,
    persistence: RestonomerPersistence
)
