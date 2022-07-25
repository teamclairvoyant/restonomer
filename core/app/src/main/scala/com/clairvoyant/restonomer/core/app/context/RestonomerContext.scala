package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.config.RestonomerContextConfig.loadConfig
import com.clairvoyant.restonomer.core.common.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.RestonomerConfigType.implicits._
import com.clairvoyant.restonomer.core.model.config.{Checkpoint, RestonomerConfigType}
import pureconfig.ConfigReader

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

  def buildCheckpoint(checkpointName: String): Checkpoint =
    buildConfig[Checkpoint](checkpointsDirectoryPath, checkpointName)

  def runCheckpoint(checkpointName: String): Unit = {
    val checkpoint = buildCheckpoint(checkpointName)
    println(checkpoint)
  }

  private def buildConfig[C <: RestonomerConfigType](configDirectoryPath: String, configName: String)(
      implicit reader: ConfigReader[C]
  ): C = {
    if (fileExists(configDirectoryPath)) {
      val configFilePath = s"$configDirectoryPath/$configName.conf"
      if (fileExists(configFilePath))
        loadConfig[C](configFilePath)
      else
        throw new RestonomerContextException(
          s"The config file for $configName does not exists under the path: $configFilePath"
        )
    } else
      throw new RestonomerContextException(
        s"The config directory path: $configDirectoryPath does not exists."
      )
  }

}
