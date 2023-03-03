package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class ApplicationConfig(sparkConfigs: Option[Map[String, String]])

object ApplicationConfig {
  implicit val config: Config[ApplicationConfig] = deriveConfig[ApplicationConfig].mapKey(toKebabCase)
}
