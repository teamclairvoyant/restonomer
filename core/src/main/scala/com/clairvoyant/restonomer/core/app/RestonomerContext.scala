package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.config.ConfigVariablesSubstitutor
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists
import pureconfig.generic.auto._

object RestonomerContext {

  val DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH = "./restonomer_context"

  def apply(restonomerContextDirectoryPath: String = DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH): RestonomerContext = {
    if (fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextDirectoryPath)
    else
      throw new RestonomerContextException(
        s"The RestonomerContext directory path: $restonomerContextDirectoryPath does not exists."
      )
  }

}

class RestonomerContext(val restonomerContextDirectoryPath: String) {

  val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"
  val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT}"

  val configVariables: Map[String, String] = loadConfigVariables(CONFIG_VARIABLES_FILE_PATH)

  def runCheckpoint(checkpointName: String): Unit = {
    val filePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointName.conf"

    val checkpoint: CheckpointConfig = loadConfigsFromFilePath[CheckpointConfig](
      filePath,
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )
    runCheckpoint(checkpoint)
  }

  def runAllCheckpoints(): Unit = {
    val checkpoints: List[CheckpointConfig] = loadConfigsFromDirectory[CheckpointConfig](
      configDirectoryPath = CHECKPOINTS_CONFIG_DIRECTORY_PATH,
      configVariablesSubstitutor = ConfigVariablesSubstitutor(configVariables = configVariables)
    )
    checkpoints.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit = new RestonomerWorkflow().run(checkpointConfig)

}
