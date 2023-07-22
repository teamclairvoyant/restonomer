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
  private lazy val rawConfig = deriveConfig[CheckpointConfig]
  val config: Config[CheckpointConfig] = rawConfig.toKebabCase
}
