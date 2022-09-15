package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.ApplicationConfig
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.generic.auto._
import pureconfig.{ConfigReader, ConfigSource}

import java.io.{File, FileNotFoundException}
import scala.annotation.tailrec
import scala.reflect.ClassTag

object RestonomerConfigurationsLoader {

  def loadApplicationConfiguration(applicationConfigFilePath: String): ApplicationConfig = {
    if (fileExists(applicationConfigFilePath))
      loadConfigFromFile[ApplicationConfig](applicationConfigFilePath)
    else
      throw new RestonomerException(s"The application config file path: $applicationConfigFilePath does not exists.")
  }

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
        throw new RestonomerException(error.prettyPrint())
    }
  }

  def loadConfigsFromDirectory[C: ClassTag](
      configDirectoryPath: String,
      configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor()
  )(implicit reader: ConfigReader[C]): List[C] = {
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
            loadConfigFromString(configVariablesSubstitutor.substituteConfigVariables(configFile)) :: configs
          )
      }
    }

    if (fileExists(configDirectoryPath))
      loadConfigsFromDirectoryHelper(new File(configDirectoryPath).listFiles().toList, List())
    else
      throw new FileNotFoundException(s"The config directory with the path: $configDirectoryPath does not exists.")
  }

  def loadConfigFromString[C](configText: String)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.string(configText).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerException(error.prettyPrint())
    }
  }

  def loadConfigsFromFilePath[C: ClassTag](
      configFilePath: String,
      configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor()
  )(implicit reader: ConfigReader[C]): C = {

    if (fileExists(configFilePath))
      loadConfigFromString(configVariablesSubstitutor.substituteConfigVariables(new File(configFilePath)))
    else
      throw new FileNotFoundException(s"The config directory with the path: $configFilePath does not exists.")
  }

}
