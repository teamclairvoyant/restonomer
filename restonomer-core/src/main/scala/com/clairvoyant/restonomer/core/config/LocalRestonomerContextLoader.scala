package com.clairvoyant.restonomer.core.config

import java.nio.file.Files
import java.nio.file.Paths
import scala.io.Source
import zio.Config
import scala.annotation.tailrec
import java.io.File

class LocalRestonomerContextLoader extends RestonomerContextLoader {

  override def fileExists(filePath: String): Boolean = Files.exists(Paths.get(filePath))

  override def readConfigFile(configFilePath: String): Source = Source.fromFile(configFilePath)

  override def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[File], configs: List[C]): List[C] =
      if (remainingConfigFiles.isEmpty) configs
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
