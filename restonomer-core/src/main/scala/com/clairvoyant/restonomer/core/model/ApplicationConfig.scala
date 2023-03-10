package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config._
import zio.config.magnolia._

case class ApplicationConfig(sparkConfigs: Option[Map[String, String]])

object ApplicationConfig {
  implicit val config: Config[ApplicationConfig] = deriveConfig[ApplicationConfig].toKebabCase
}
