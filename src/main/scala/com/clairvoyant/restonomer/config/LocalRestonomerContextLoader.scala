package com.clairvoyant.restonomer.config

import zio.Config

import java.io.File
import java.nio.file.{Files, Paths}
import scala.annotation.tailrec
import scala.io.Source

class LocalRestonomerContextLoader extends RestonomerContextLoader {

  override def fileExists(filePath: String): Boolean = Files.exists(Paths.get(filePath))

  override def readConfigFile(configFilePath: String): Source = Source.fromFile(configFilePath)

  override def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[File], configs: List[C]): List[C] =
      if (remainingConfigFiles.isEmpty)
        configs
      else {
        val configFile = remainingConfigFiles.head

        if (configFile.isDirectory)
          loadConfigsFromDirectoryHelper(configFile.listFiles().toList ++ remainingConfigFiles.tail, configs)
        else
          loadConfigsFromDirectoryHelper(
            remainingConfigFiles.tail,
            loadConfigFromFile(configFile.getPath, config) :: configs
          )
      }

    loadConfigsFromDirectoryHelper(new File(configDirectoryPath).listFiles().toList, List())
  }

}
