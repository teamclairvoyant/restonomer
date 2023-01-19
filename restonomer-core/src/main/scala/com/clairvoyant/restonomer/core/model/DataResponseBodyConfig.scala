package com.clairvoyant.restonomer.core.model

sealed trait DataResponseBodyConfig

case class JSON(
    dataColumnName: Option[String] = None
) extends DataResponseBodyConfig
