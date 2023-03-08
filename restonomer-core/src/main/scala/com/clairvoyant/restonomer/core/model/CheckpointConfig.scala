package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)

object CheckpointConfig {
  implicit val config: Config[CheckpointConfig] = deriveConfig[CheckpointConfig].toKebabCase
}
