package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader.*
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import zio.Config
import zio.config.magnolia.*

import java.io.FileNotFoundException

object RestonomerContext {

  private val DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH = "./restonomer_context"

  def apply(
      restonomerContextDirectoryPath: String = DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH,
      configVariablesFromApplicationArgs: Map[String, String] = Map()
  ): RestonomerContext =
    if (fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextDirectoryPath, configVariablesFromApplicationArgs)
    else
      throw new RestonomerException(
        s"The restonomerContextDirectoryPath: $restonomerContextDirectoryPath does not exists."
      )

}

class RestonomerContext(
    val restonomerContextDirectoryPath: String,
    val configVariablesFromApplicationArgs: Map[String, String]
) {
  private val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  private val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"
  private val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/checkpoints"

  private val configVariablesFromFile = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (fileExists(CONFIG_VARIABLES_FILE_PATH))
      loadConfigFromFile[Map[String, String]](CONFIG_VARIABLES_FILE_PATH, deriveConfig[Map[String, String]])
    else
      Map[String, String]()
  }

  private val applicationConfig = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (fileExists(APPLICATION_CONFIG_FILE_PATH))
      loadConfigFromFile[ApplicationConfig](APPLICATION_CONFIG_FILE_PATH, ApplicationConfig.config)
    else
      throw new FileNotFoundException(
        s"The application config file with the path: $APPLICATION_CONFIG_FILE_PATH does not exists."
      )
  }

  given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] =
    Some(ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs))

  def runCheckpoint(checkpointFilePath: String): Unit = {
    val absoluteCheckpointFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointFilePath"

    if (fileExists(absoluteCheckpointFilePath))
      runCheckpoint(loadConfigFromFile[CheckpointConfig](absoluteCheckpointFilePath, CheckpointConfig.config))
    else
      throw new FileNotFoundException(
        s"The checkpoint file with the path: $absoluteCheckpointFilePath does not exists."
      )
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit = {
    val absoluteCheckpointsDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath"

    if (fileExists(absoluteCheckpointsDirectoryPath))
      runCheckpoints(
        loadConfigsFromDirectory[CheckpointConfig](absoluteCheckpointsDirectoryPath, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The config directory with the path: $absoluteCheckpointsDirectoryPath does not exists."
      )
  }

  def runAllCheckpoints(): Unit =
    if (fileExists(CHECKPOINTS_CONFIG_DIRECTORY_PATH))
      runCheckpoints(
        loadConfigsFromDirectory[CheckpointConfig](CHECKPOINTS_CONFIG_DIRECTORY_PATH, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The config directory with the path: $CHECKPOINTS_CONFIG_DIRECTORY_PATH does not exists."
      )

  private def runCheckpoints(checkpointConfigs: List[CheckpointConfig]): Unit =
    checkpointConfigs.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit =
    RestonomerWorkflow(applicationConfig).run(checkpointConfig)

}
