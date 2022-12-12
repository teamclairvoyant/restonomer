package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.ConfigReader
import pureconfig.generic.auto._
import sttp.client3.UriContext
import sttp.model.Uri

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
  implicit val urlReader: ConfigReader[Uri] = ConfigReader[String].map(url => uri"$url")

  private val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  private val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"
  private val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/checkpoints"

  private val configVariablesFromFile = {
    implicit val configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor(
      configVariablesFromApplicationArgs = configVariablesFromApplicationArgs
    )

    loadConfigsFromFilePath[Map[String, String]](
      configFilePath = CONFIG_VARIABLES_FILE_PATH,
      fileNotFoundAction = () => Map()
    )
  }

  implicit private val configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor(
    configVariablesFromFile = configVariablesFromFile,
    configVariablesFromApplicationArgs = configVariablesFromApplicationArgs
  )

  private val applicationConfig = loadConfigsFromFilePath[ApplicationConfig](
    configFilePath = APPLICATION_CONFIG_FILE_PATH,
    fileNotFoundAction =
      () =>
        throw new FileNotFoundException(
          s"The application config file with the path: $APPLICATION_CONFIG_FILE_PATH does not exists."
        )
  )

  def runCheckpoint(checkpointFilePath: String): Unit = {
    val absoluteCheckpointFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointFilePath"

    runCheckpoint(
      loadConfigsFromFilePath[CheckpointConfig](
        configFilePath = absoluteCheckpointFilePath,
        fileNotFoundAction =
          () =>
            throw new FileNotFoundException(
              s"The checkpoint file with the path: $absoluteCheckpointFilePath does not exists."
            )
      )
    )
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit =
    runCheckpoints(
      loadConfigsFromDirectory[CheckpointConfig](
        configDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath"
      )
    )

  def runAllCheckpoints(): Unit =
    runCheckpoints(
      loadConfigsFromDirectory[CheckpointConfig](
        configDirectoryPath = CHECKPOINTS_CONFIG_DIRECTORY_PATH
      )
    )

  private def runCheckpoints(checkpointConfigs: List[CheckpointConfig]): Unit =
    checkpointConfigs.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }

  private def runCheckpoint(checkpointConfig: CheckpointConfig): Unit =
    RestonomerWorkflow(applicationConfig)
      .run(checkpointConfig)

}
