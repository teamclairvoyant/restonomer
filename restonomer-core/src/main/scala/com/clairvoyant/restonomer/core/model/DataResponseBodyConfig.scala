package com.clairvoyant.restonomer.core.model

import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait DataResponseBodyConfig

case class CSV() extends DataResponseBodyConfig

case class JSON(
    dataColumnName: Option[String] = None
) extends DataResponseBodyConfig
