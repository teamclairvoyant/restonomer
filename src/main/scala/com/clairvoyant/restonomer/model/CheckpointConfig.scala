package com.clairvoyant.restonomer.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig,
    sparkConfigs: Option[Map[String, String]]
)

object CheckpointConfig {
  private lazy val rawConfig = deriveConfig[CheckpointConfig]
  val config: Config[CheckpointConfig] = rawConfig.toKebabCase
}
