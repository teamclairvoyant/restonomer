package com.clairvoyant.restonomer.core.app.config

import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.RestonomerConfigType
import pureconfig._
import pureconfig.generic.auto._

import java.io.File

object RestonomerContextConfig {

  def loadConfig[C <: RestonomerConfigType](configFile: File): C =
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.toString())
    }

  def loadConfig[C <: RestonomerConfigType](configFilePath: String): C = loadConfig(new File(configFilePath))

}
