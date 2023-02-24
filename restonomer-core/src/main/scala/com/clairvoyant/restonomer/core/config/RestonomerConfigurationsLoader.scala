package com.clairvoyant.restonomer.core.config

import zio.config.typesafe._
import zio.{ConfigProvider, _}

import java.io.File
import scala.annotation.tailrec

object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](configFilePath: String)(
      implicit config: Config[C],
      configVariablesSubstitutor: ConfigVariablesSubstitutor
  ): C =
    Unsafe.unsafe(implicit u => {
      zio.Runtime.default.unsafe
        .run(
          ConfigProvider
            .fromHoconString(
              configVariablesSubstitutor.substituteConfigVariables(new File(configFilePath))
            )
            .load(config)
        )
        .getOrThrowFiberFailure()
    })

  def loadConfigsFromDirectory[C](configDirectoryPath: String)(
      implicit config: Config[C],
      configVariablesSubstitutor: ConfigVariablesSubstitutor
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
