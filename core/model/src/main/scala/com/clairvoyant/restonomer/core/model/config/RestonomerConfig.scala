package com.clairvoyant.restonomer.core.model.config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

class RestonomerConfig

object RestonomerConfig {

  object implicits {
    implicit val checkpointConfigReader: ConfigReader[CheckpointConfig] = deriveReader[CheckpointConfig]
    implicit val httpConfigReader: ConfigReader[HttpConfig] = deriveReader[HttpConfig]
    implicit val requestConfigReader: ConfigReader[RequestConfig] = deriveReader[RequestConfig]
  }

}
