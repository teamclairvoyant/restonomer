package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
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
  ): RestonomerContext = {
    if (fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextDirectoryPath, configVariablesFromApplicationArgs)
    else
      throw new RestonomerException(
        s"The RestonomerContext directory path: $restonomerContextDirectoryPath does not exists."
      )
  }

}

class RestonomerContext(
    val restonomerContextDirectoryPath: String,
    val configVariablesFromApplicationArgs: Map[String, String]
) {
  implicit val urlReader: ConfigReader[Uri] = ConfigReader[String].map(url => uri"$url")

  private val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  private val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"

  private val CHECKPOINTS_CONFIG_DIRECTORY_PATH =
    s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT}"

  private val configVariablesFromFile = loadConfigVariablesFromFile(CONFIG_VARIABLES_FILE_PATH)

  implicit private val configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor(
    configVariablesFromFile = configVariablesFromFile,
    configVariablesFromApplicationArgs = configVariablesFromApplicationArgs
  )

  private val applicationConfig = loadApplicationConfig(APPLICATION_CONFIG_FILE_PATH)

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
      val checkpointConfig = loadConfigsFromFilePath[CheckpointConfig](absoluteCheckpointFilePath)
      runCheckpoint(checkpointConfig)
    } else
      throw new FileNotFoundException(s"The checkpoint file with the path: $checkpointFilePath does not exists.")
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath"
    )
    runCheckpoints(checkpoints)
  }

  def runAllCheckpoints(): Unit = {
    val checkpoints = loadConfigsFromDirectory[CheckpointConfig](CHECKPOINTS_CONFIG_DIRECTORY_PATH)
    runCheckpoints(checkpoints)
  }

}
