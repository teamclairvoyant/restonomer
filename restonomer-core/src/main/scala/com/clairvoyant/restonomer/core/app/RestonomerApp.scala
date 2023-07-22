package com.clairvoyant.restonomer.core.app

object RestonomerApp extends App {

  val arguments =
    args(0)
      .split(";")
      .map(arg => arg.split("~")(0) -> arg.split("~")(1))
      .toMap

  val restonomerContextDirPath = arguments("restonomer_context_dir_path")
  val checkpointFilePath = arguments("checkpoint_file_path")

  val restonomerContext = RestonomerContext(restonomerContextDirPath)
  restonomerContext.runCheckpoint(checkpointFilePath)
}
