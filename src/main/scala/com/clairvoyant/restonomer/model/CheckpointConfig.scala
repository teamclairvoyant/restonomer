package com.clairvoyant.restonomer.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig,
    sparkConfigs: Map[String, String] = Map.empty
)

object CheckpointConfig {
  private lazy val rawConfig = deriveConfig[CheckpointConfig]
  val config: Config[CheckpointConfig] = rawConfig.toKebabCase
}
