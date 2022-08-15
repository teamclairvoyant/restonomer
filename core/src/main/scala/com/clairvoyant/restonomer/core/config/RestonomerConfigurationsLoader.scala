package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.{ConfigReader, ConfigSource}

import java.io.{File, FileNotFoundException}
import scala.reflect.ClassTag

object RestonomerConfigurationsLoader {

  def loadConfigVariables(configVariablesFilePath: String): Map[String, String] = {
    if (fileExists(configVariablesFilePath))
      loadConfigFromFile[Map[String, String]](configVariablesFilePath)
    else
      Map()
  }

  def loadConfigFromFile[C](configFilePath: String)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.file(new File(configFilePath)).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

  def loadConfigsFromDirectory[C: ClassTag](
      configDirectoryPath: String,
      configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor()
  )(implicit reader: ConfigReader[C]): List[C] = {
    if (fileExists(configDirectoryPath)) {
      new File(configDirectoryPath)
        .listFiles()
        .map(configVariablesSubstitutor.substituteConfigVariables)
        .map(loadConfigFromString[C])
        .toList
    } else
      throw new FileNotFoundException(s"The config directory with the path: $configDirectoryPath does not exists.")
  }

  def loadConfigFromString[C](configText: String)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.string(configText).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

}
