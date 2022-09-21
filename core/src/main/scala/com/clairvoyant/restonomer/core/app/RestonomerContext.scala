package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.generic.auto._

import java.io.FileNotFoundException

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

  val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"
  val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT}"

  val configVariables: Map[String, String] = loadConfigVariables(CONFIG_VARIABLES_FILE_PATH)

  val applicationConfig: ApplicationConfig = loadApplicationConfig(
    applicationConfigFilePath = APPLICATION_CONFIG_FILE_PATH,
    configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
  )

  private def runCheckpoint(checkpointConfig: CheckpointConfig): Unit =
    RestonomerWorkflow(applicationConfig).run(checkpointConfig)

  private def runCheckpoints(checkpointConfigs: List[CheckpointConfig]): Unit = {
    checkpointConfigs.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpoint(checkpointFilePath: String): Unit = {
    val absoluteCheckpointFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointFilePath"

    if (fileExists(absoluteCheckpointFilePath)) {
      val checkpointConfig = loadConfigsFromFilePath[CheckpointConfig](
        configFilePath = absoluteCheckpointFilePath,
        configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
      )

      runCheckpoint(checkpointConfig)
    } else
      throw new FileNotFoundException(s"The checkpoint file with the path: $checkpointFilePath does not exists.")
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath",
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )

    runCheckpoints(checkpoints)
  }

  def runAllCheckpoints(): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = CHECKPOINTS_CONFIG_DIRECTORY_PATH,
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )

    runCheckpoints(checkpoints)
  }

}
