package com.clairvoyant.restonomer.core.model

import zio.Config
import zio.config._
import zio.config.magnolia._

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)

object CheckpointConfig {
  lazy val rawConfig: Config[CheckpointConfig] = deriveConfig[CheckpointConfig]
  implicit val config: Config[CheckpointConfig] = deriveConfig[CheckpointConfig].toKebabCase
}
