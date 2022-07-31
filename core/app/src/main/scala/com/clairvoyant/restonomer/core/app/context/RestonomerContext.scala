package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.workflow.RestonomerWorkflow
import com.clairvoyant.restonomer.core.common.enums.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.common.util.ConfigUtil.loadConfigsFromDirectory
import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.{CheckpointConfig, RestonomerContextConfig}
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

  val configs: RestonomerContextConfig = {

    // CHECKPOINT
    val checkpointConfigs = loadConfigsFromDirectory[CheckpointConfig](configDirectoryPath =
      s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT.configDirectoryName}"
    )

    RestonomerContextConfig(checkpoints = checkpointConfigs)
  }

  def runCheckpoint(checkpointName: String): Unit =
    RestonomerWorkflow().run {
      configs.checkpoints
        .find(_.name == checkpointName) match {
        case Some(checkpointConfig) =>
          checkpointConfig
        case None =>
          throw new RestonomerContextException(s"The checkpoint: $checkpointName does not exists.")
      }
    }

}
