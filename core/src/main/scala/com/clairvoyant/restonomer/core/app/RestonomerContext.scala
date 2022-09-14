package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.generic.auto._

object RestonomerContext {

  val DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH = "./restonomer_context"

  def apply(restonomerContextDirectoryPath: String = DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH): RestonomerContext = {
    if (fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextDirectoryPath)
    else
      throw new RestonomerException(
        s"The RestonomerContext directory path: $restonomerContextDirectoryPath does not exists."
      )
  }

}

class RestonomerContext(val restonomerContextDirectoryPath: String) {

  val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"
  val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT}"

  val applicationConfigurations: Map[String, String] = loadApplicationConfigurations(APPLICATION_CONFIG_FILE_PATH)

  val configVariables: Map[String, String] = loadConfigVariables(CONFIG_VARIABLES_FILE_PATH)

  val checkpoints: List[CheckpointConfig] = loadConfigsFromDirectory[CheckpointConfig](
    configDirectoryPath = CHECKPOINTS_CONFIG_DIRECTORY_PATH,
    configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
  )

  def runCheckpoint(checkpointName: String): Unit =
    checkpoints
      .find(_.name == checkpointName) match {
      case Some(checkpointConfig) =>
        runCheckpoint(checkpointConfig)
      case None =>
        throw new RestonomerException(s"The checkpoint: $checkpointName does not exists.")
    }

  def runAllCheckpoints(): Unit = {
    checkpoints.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit =
    RestonomerWorkflow(applicationConfigurations).run(checkpointConfig)

}
