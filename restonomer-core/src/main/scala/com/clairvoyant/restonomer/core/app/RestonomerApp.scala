package com.clairvoyant.restonomer.core.app

import cats.implicits.*
import com.clairvoyant.restonomer.core.app.RestonomerContext
import com.monovore.decline.{CommandApp, Opts}

object RestonomerApp
    extends CommandApp(
      name = "restonomer-app",
      header = "Dumps the data from the REST API provided in the checkpoint",
      main = {
        val restonomerContextDirPathOpt: Opts[String] = Opts
          .option[String](long = "restonomer_context_dir_path", help = "Full path to the restonomer context directory")

        val checkpointFilePathOpt: Opts[String] = Opts
          .option[String](long = "checkpoint_file_path", help = "Relative path of the checkpoint file to be executed")

        (restonomerContextDirPathOpt, checkpointFilePathOpt).mapN { (restonomerContextDirPath, checkpointFilePath) =>
          val restonomerContext = RestonomerContext(restonomerContextDirPath)
          restonomerContext.runCheckpoint(checkpointFilePath)
        }
      }
    )
