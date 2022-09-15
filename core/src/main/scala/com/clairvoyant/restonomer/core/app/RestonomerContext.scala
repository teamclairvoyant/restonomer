package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
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

  val applicationConfig: ApplicationConfig = loadApplicationConfiguration(APPLICATION_CONFIG_FILE_PATH)

  val configVariables: Map[String, String] = loadConfigVariables(CONFIG_VARIABLES_FILE_PATH)

  def runAllCheckpointsInDir(dirPath: String): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$dirPath",
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )

    checkpoints.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpointWithPath(checkpointPath: String): Unit = {
    val checkpoint = loadConfigsFromFilePath[CheckpointConfig](
      configFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointPath",
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )
    runCheckpoint(checkpoint)
  }

  def runAllCheckpoints(): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = CHECKPOINTS_CONFIG_DIRECTORY_PATH,
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )

    checkpoints.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit =
    RestonomerWorkflow(applicationConfig).run(checkpointConfig)

}
