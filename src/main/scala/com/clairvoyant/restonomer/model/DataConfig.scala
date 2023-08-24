package com.clairvoyant.restonomer.model

import zio.config.*
import zio.config.magnolia.*

case class DataConfig(
    dataRequest: RequestConfig,
    dataResponse: DataResponseConfig
)
