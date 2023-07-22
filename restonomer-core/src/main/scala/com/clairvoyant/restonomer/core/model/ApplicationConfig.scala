package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config._
import zio.config.magnolia._

case class ApplicationConfig(sparkConfigs: Option[Map[String, String]])

object ApplicationConfig {
  private lazy val rawConfig = deriveConfig[ApplicationConfig]
  val config: Config[ApplicationConfig] = rawConfig.toKebabCase
}
