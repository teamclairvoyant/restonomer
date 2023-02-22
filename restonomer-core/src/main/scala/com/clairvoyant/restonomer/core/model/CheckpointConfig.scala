package com.clairvoyant.restonomer.core.model

import zio.config._
import zio.config.magnolia._

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)

object CheckpointConfig {

  implicit val checkpointConfigDescriptor: ConfigDescriptor[CheckpointConfig] =
    descriptorForPureConfig[CheckpointConfig]
      .mapKey(toKebabCase)

}
