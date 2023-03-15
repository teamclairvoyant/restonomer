package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class ApplicationConfig(sparkConfigs: Option[Map[String, String]])

object ApplicationConfig {
  private lazy val rawConfig = deriveConfig[ApplicationConfig]
  val config: Config[ApplicationConfig] = rawConfig.toKebabCase
}
