package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config.*
import zio.config.magnolia.*

object CheckpointConfig {
  given config: Config[CheckpointConfig] = deriveConfig[CheckpointConfig].mapKey(toKebabCase)
}

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)
