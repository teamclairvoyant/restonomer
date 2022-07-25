package com.clairvoyant.restonomer.core.model.config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

class RestonomerConfigType

object RestonomerConfigType {

  object implicits {
    implicit val checkpointReader: ConfigReader[Checkpoint] = deriveReader[Checkpoint]
  }

}
