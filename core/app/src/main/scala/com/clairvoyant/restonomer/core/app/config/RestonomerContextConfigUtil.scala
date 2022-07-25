package com.clairvoyant.restonomer.core.app.config

import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import pureconfig._

import java.io.File

object RestonomerContextConfigUtil {

  def buildConfig[C](configDirectoryPath: String, configName: String)(
      implicit reader: ConfigReader[C]
  ): C = {
    if (fileExists(configDirectoryPath)) {
      val configFilePath = s"$configDirectoryPath/$configName.conf"
      if (fileExists(configFilePath))
        loadConfig[C](configFilePath)
      else
        throw new RestonomerContextException(
          s"The config file for $configName does not exists under the path: $configFilePath"
        )
    } else
      throw new RestonomerContextException(
        s"The config directory path: $configDirectoryPath does not exists."
      )
  }

  def loadConfig[C](configFile: File)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

  def loadConfig[C](configFilePath: String)(implicit reader: ConfigReader[C]): C = loadConfig(new File(configFilePath))

}
