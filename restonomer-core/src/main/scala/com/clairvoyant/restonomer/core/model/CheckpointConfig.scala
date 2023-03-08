package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config._
import zio.config.magnolia._

object CheckpointConfig {
  implicit val config: Config[CheckpointConfig] = deriveConfig[CheckpointConfig].mapKey(toKebabCase)
}

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)
