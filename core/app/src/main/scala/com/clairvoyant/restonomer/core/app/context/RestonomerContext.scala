package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.config.RestonomerContextConfig.buildConfig
import com.clairvoyant.restonomer.core.common.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import com.clairvoyant.restonomer.core.model.config.RestonomerConfig.implicits.checkpointConfigReader

object RestonomerContext {
  val DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH = "./restonomer_context"

  def apply(restonomerContextDirectoryPath: String = DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH): RestonomerContext = {
    if (fileExists(restonomerContextDirectoryPath)) {
      new RestonomerContext(restonomerContextDirectoryPath)
    } else {
      throw new RestonomerContextException(
        s"The RestonomerContext directory path: $restonomerContextDirectoryPath does not exists."
      )
    }
  }

}

class RestonomerContext(val restonomerContextDirectoryPath: String) {
  val checkpointsDirectoryPath: String = s"$restonomerContextDirectoryPath/checkpoints"

  def buildCheckpoint(checkpointName: String): CheckpointConfig =
    buildConfig[CheckpointConfig](checkpointsDirectoryPath, checkpointName)

  def runCheckpoint(checkpointName: String): Unit = {
    val checkpoint = buildCheckpoint(checkpointName)

    println(checkpoint)
  }

}
