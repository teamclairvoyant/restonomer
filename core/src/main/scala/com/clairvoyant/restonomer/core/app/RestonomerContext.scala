package com.clairvoyant.restonomer.core.app

import com.clairvoyant.restonomer.core.common.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
import com.clairvoyant.restonomer.core.util.ConfigUtil.loadConfigsFromDirectory
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

  val checkpointConfigs: List[CheckpointConfig] = loadConfigsFromDirectory[CheckpointConfig](configDirectoryPath =
    s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT}"
  )

  def runCheckpoint(checkpointName: String): Unit =
    checkpointConfigs
      .find(_.name == checkpointName) match {
      case Some(checkpointConfig) =>
        runCheckpoint(checkpointConfig)
      case None =>
        throw new RestonomerContextException(s"The checkpoint: $checkpointName does not exists.")
    }

  def runAllCheckpoints(): Unit = {
    checkpointConfigs.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }
  }

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit = new RestonomerWorkflow().run(checkpointConfig)

}
