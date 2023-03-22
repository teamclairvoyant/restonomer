package com.clairvoyant.restonomer.core.model

import org.apache.spark.sql.SaveMode
import zio.Config
import zio.config._
import zio.config.magnolia._

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)

object CheckpointConfig {

  implicit val saveModeConfig: DeriveConfig[SaveMode] = DeriveConfig[String].map(saveModeString =>
    SaveMode.valueOf(saveModeString)
  )

  lazy val rawConfig: Config[CheckpointConfig] = deriveConfig[CheckpointConfig]
  implicit val config: Config[CheckpointConfig] = rawConfig.toKebabCase
}
