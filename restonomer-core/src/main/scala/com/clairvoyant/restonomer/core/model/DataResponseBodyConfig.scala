package com.clairvoyant.restonomer.core.model

import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait DataResponseBodyConfig

case class CSV() extends DataResponseBodyConfig

case class JSON(
    dataColumnName: Option[String] = None,
    dateFormat: String = "yyyy-MM-dd",
    inferSchema: Boolean = true,
    locale: String = "en-US",
    multiLine: Boolean = false,
    originalSchema: Option[String] = None,
    primitivesAsString: Boolean = false,
    timestampFormat: String = "yyyy-MM-dd HH:mm:ss",
    timestampNTZFormat: String = "yyyy-MM-dd'T'HH:mm:ss[.SSS]"
) extends DataResponseBodyConfig
