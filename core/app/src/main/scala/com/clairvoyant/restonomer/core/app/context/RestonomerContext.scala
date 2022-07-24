package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.config.RestonomerContextConfig.loadConfig
import com.clairvoyant.restonomer.core.common.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.Checkpoint

import java.io.File

object RestonomerContext {
  val DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH = "./restonomer_context"

  def apply(restonomerContextDirectoryPath: String = DEFAULT_RESTONOMER_CONTEXT_DIRECTORY_PATH): RestonomerContext = {
    if (fileExists(restonomerContextDirectoryPath)) {
      new RestonomerContext(new File(restonomerContextDirectoryPath))
    } else {
      throw new RestonomerContextException(
        s"The RestonomerContext directory path: $restonomerContextDirectoryPath does not exists."
      )
    }
  }

}

class RestonomerContext(val restonomerContextDirectory: File) {

  val checkpointsDirectoryPath: String = s"${restonomerContextDirectory.getPath}/checkpoints"

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

  def buildCheckpoint(checkpointFile: File): Checkpoint = buildCheckpoint(checkpointFile.getName.split("\\.conf").head)

  def runCheckpoint(checkpointName: String): Unit = {
    val checkpoint = buildCheckpoint(checkpointName)

    println(checkpoint)
  }

}
