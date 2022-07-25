package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.config.RestonomerContextConfig.loadConfig
import com.clairvoyant.restonomer.core.common.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.Checkpoint
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

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

  implicit val checkpointReader: ConfigReader[Checkpoint] = deriveReader[Checkpoint]

  def buildCheckpoint(checkpointName: String): Checkpoint = {
    if (fileExists(checkpointsDirectoryPath)) {
      val checkpointConfigFilePath = s"$checkpointsDirectoryPath/$checkpointName.conf"
      if (fileExists(checkpointConfigFilePath))
        loadConfig[Checkpoint](checkpointConfigFilePath)
      else
        throw new RestonomerContextException(
          s"The config file for checkpoint: $checkpointName does not exists under the path: $checkpointConfigFilePath"
        )
    } else
      throw new RestonomerContextException(
        s"The checkpoints directory path: $checkpointsDirectoryPath does not exists."
      )
  }

  def runCheckpoint(checkpointName: String): Unit = {
    val checkpoint = buildCheckpoint(checkpointName)

    println(checkpoint)
  }

}
