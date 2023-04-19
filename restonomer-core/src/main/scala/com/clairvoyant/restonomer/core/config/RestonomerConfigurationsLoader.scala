package com.clairvoyant.restonomer.core.config

import zio.config.typesafe.*
import zio.{ConfigProvider, *}

import java.io.File
import scala.annotation.tailrec
import scala.io.Source

object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](configFilePath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): C = {
    val configFileSource = Source.fromFile(new File(configFilePath))

    val configString =
      try configFileSource.mkString
      finally configFileSource.close()

    Unsafe.unsafe(implicit u => {
      zio.Runtime.default.unsafe
        .run(
          ConfigProvider
            .fromHoconString(
              configVariablesSubstitutor
                .map(_.substituteConfigVariables(configString))
                .getOrElse(configString)
            )
            .load(config)
        )
        .getOrThrowFiberFailure()
    })
  }

  def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[File], configs: List[C]): List[C] = {
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
    }

    loadConfigsFromDirectoryHelper(new File(configDirectoryPath).listFiles().toList, List())
  }

}
