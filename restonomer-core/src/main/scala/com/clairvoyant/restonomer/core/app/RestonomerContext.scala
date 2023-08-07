package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.config.{ConfigVariablesSubstitutor, GCSRestonomerContextLoader, LocalRestonomerContextLoader, RestonomerContextLoader}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.google.cloud.storage.{Storage, StorageOptions}
import zio.Config
import zio.config.magnolia.*

import java.io.FileNotFoundException

object RestonomerContext {

  def apply(
      restonomerContextDirectoryPath: String,
      configVariablesFromApplicationArgs: Map[String, String] = Map()
  ): RestonomerContext = {
    val restonomerContextLoader =
      if (restonomerContextDirectoryPath.startsWith("gs://")) {
        given gcsStorageClient: Storage = StorageOptions.getDefaultInstance().getService()
        GCSRestonomerContextLoader()
      } else
        LocalRestonomerContextLoader()

    if (restonomerContextLoader.fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextLoader, restonomerContextDirectoryPath, configVariablesFromApplicationArgs)
    else
      throw new RestonomerException(
        s"The restonomerContextDirectoryPath: $restonomerContextDirectoryPath does not exists."
      )
  }

}

class RestonomerContext(
    val restonomerContextLoader: RestonomerContextLoader,
    val restonomerContextDirectoryPath: String,
    val configVariablesFromApplicationArgs: Map[String, String]
) {
  private val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  private val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"
  private val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/checkpoints"

  private val configVariablesFromFile = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (restonomerContextLoader.fileExists(CONFIG_VARIABLES_FILE_PATH))
      restonomerContextLoader
        .loadConfigFromFile[Map[String, String]](CONFIG_VARIABLES_FILE_PATH, deriveConfig[Map[String, String]])
    else
      Map[String, String]()
  }

  private val applicationConfig = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (restonomerContextLoader.fileExists(APPLICATION_CONFIG_FILE_PATH))
      restonomerContextLoader
        .loadConfigFromFile[ApplicationConfig](APPLICATION_CONFIG_FILE_PATH, ApplicationConfig.config)
    else
      throw new FileNotFoundException(
        s"The application config file with the path: $APPLICATION_CONFIG_FILE_PATH does not exists."
      )
  }

  given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] =
    Some(ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs))

  def runCheckpoint(checkpointFilePath: String): Unit = {
    val absoluteCheckpointFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointFilePath"

    if (restonomerContextLoader.fileExists(absoluteCheckpointFilePath))
      runCheckpoint(
        restonomerContextLoader
          .loadConfigFromFile[CheckpointConfig](absoluteCheckpointFilePath, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The checkpoint file with the path: $absoluteCheckpointFilePath does not exists."
      )
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit = {
    val absoluteCheckpointsDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath"

    if (restonomerContextLoader.fileExists(absoluteCheckpointsDirectoryPath))
      runCheckpoints(
        restonomerContextLoader
          .loadConfigsFromDirectory[CheckpointConfig](absoluteCheckpointsDirectoryPath, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The config directory with the path: $absoluteCheckpointsDirectoryPath does not exists."
      )
  }

  def runAllCheckpoints(): Unit =
    if (restonomerContextLoader.fileExists(CHECKPOINTS_CONFIG_DIRECTORY_PATH))
      runCheckpoints(
        restonomerContextLoader
          .loadConfigsFromDirectory[CheckpointConfig](CHECKPOINTS_CONFIG_DIRECTORY_PATH, CheckpointConfig.config)
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
