package com.clairvoyant.restonomer.core.model

import org.apache.spark.sql.SaveMode
import zio.Config
import zio.config.*
import zio.config.magnolia.*

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)

object CheckpointConfig {
  lazy val rawConfig: Config[CheckpointConfig] = deriveConfig[CheckpointConfig]
  implicit val config: Config[CheckpointConfig] = rawConfig.toKebabCase
}
