package com.clairvoyant.restonomer.core.app.config

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import pureconfig._
import pureconfig.generic.auto._

import java.io.File

object RestonomerContextConfig {

  def loadConfig[C](configFile: File): C = {
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

  def loadConfig[C](configFilePath: String): C = loadConfig(new File(configFilePath))

}
