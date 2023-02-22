package com.clairvoyant.restonomer.core.config

import zio.Runtime.default
import zio.Unsafe
import zio.config._
import zio.config.typesafe._

import java.io.File
import scala.annotation.tailrec

object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](configFilePath: String)(
      implicit configDescriptor: ConfigDescriptor[C],
      configVariablesSubstitutor: ConfigVariablesSubstitutor
  ): C =
    Unsafe.unsafe(implicit u => {
      default.unsafe
        .run(
          read(
            configDescriptor from ConfigSource.fromHoconString(
              configVariablesSubstitutor.substituteConfigVariables(new File(configFilePath))
            )
          )
        )
        .getOrThrowFiberFailure()
    })

  def loadConfigsFromDirectory[C](configDirectoryPath: String)(
      implicit configDescriptor: ConfigDescriptor[C],
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
