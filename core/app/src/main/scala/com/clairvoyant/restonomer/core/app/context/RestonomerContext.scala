package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.app.context.RestonomerContextConfigUtil.readConfigs
import com.clairvoyant.restonomer.core.app.workflow.RestonomerWorkflow
import com.clairvoyant.restonomer.core.common.enums.RestonomerContextConfigTypes
import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.{AuthenticationConfig, CheckpointConfig, RequestConfig, RestonomerContextConfig}
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
  implicit val requestConfigReader: ConfigReader[RequestConfig] = deriveReader[RequestConfig]
  implicit val authenticationConfigReader: ConfigReader[AuthenticationConfig] = deriveReader[AuthenticationConfig]

  val configs: RestonomerContextConfig = {

    // CHECKPOINT
    val checkpointConfigs = readConfigs[CheckpointConfig](configDirectoryPath =
      s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.CHECKPOINT.configDirectoryName}"
    )

    // REQUEST
    val requestConfigs = readConfigs[RequestConfig](configDirectoryPath =
      s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.REQUEST.configDirectoryName}"
    )

    // AUTHENTICATION
    val authenticationConfigs = readConfigs[AuthenticationConfig](configDirectoryPath =
      s"$restonomerContextDirectoryPath/${RestonomerContextConfigTypes.AUTHENTICATION.configDirectoryName}"
    )

    RestonomerContextConfig(
      checkpoints = checkpointConfigs,
      requests = requestConfigs,
      authentications = authenticationConfigs
    )
  }

  def runCheckpoint(checkpointName: String): Unit = {
    val restonomerWorkflow = new RestonomerWorkflow(this)
    restonomerWorkflow.run(checkpointName)
  }

}
