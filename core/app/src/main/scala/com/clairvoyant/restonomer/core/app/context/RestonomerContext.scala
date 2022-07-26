package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.config.RestonomerContextConfigUtil.readConfigs
import com.clairvoyant.restonomer.core.common.enums.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.{CheckpointConfig, RestonomerContextConfig}
import pureconfig._
import pureconfig.generic.semiauto.deriveReader

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
  implicit val checkpointConfigReader: ConfigReader[CheckpointConfig] = deriveReader[CheckpointConfig]

  val configs: RestonomerContextConfig = initRestonomerContextConfig()

  private def initRestonomerContextConfig(): RestonomerContextConfig = {
    // CHECKPOINT
    val checkpointConfigs = readConfigs[CheckpointConfig](configDirectoryPath =
      s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT.configDirectoryName}"
    )

    RestonomerContextConfig(checkpoints = checkpointConfigs)
  }

  def runCheckpoint(checkpointName: String): Unit = {
    println(
      this.configs.checkpoints
        .map(checkpoints => checkpoints.filter(_.name == checkpointName).head)
        .getOrElse(throw new RestonomerContextException("Checkpoint not found"))
    )
  }

}
