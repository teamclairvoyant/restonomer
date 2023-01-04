package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.exception.RestonomerException
import pureconfig.{ConfigReader, ConfigSource}

import java.io.File
import scala.annotation.tailrec

object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](
      configFilePath: String
  )(
      implicit configVariablesSubstitutor: ConfigVariablesSubstitutor,
      reader: ConfigReader[C]
  ): C =
    ConfigSource
      .string(configVariablesSubstitutor.substituteConfigVariables(new File(configFilePath)))
      .load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerException(error.prettyPrint())
    }

  def loadConfigsFromDirectory[C](configDirectoryPath: String)(
      implicit configVariablesSubstitutor: ConfigVariablesSubstitutor,
      reader: ConfigReader[C]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[File], configs: List[C]): List[C] = {
      if (remainingConfigFiles.isEmpty)
        configs
      else {
        val configFile = remainingConfigFiles.head

        if (configFile.isDirectory)
          loadConfigsFromDirectoryHelper(configFile.listFiles().toList ++ remainingConfigFiles.tail, configs)
        else
          loadConfigsFromDirectoryHelper(
            remainingConfigFiles.tail,
            loadConfigFromFile(configFile.getPath) :: configs
          )
      }
    }

    loadConfigsFromDirectoryHelper(new File(configDirectoryPath).listFiles().toList, List())
  }

}
