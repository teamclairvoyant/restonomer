package com.clairvoyant.restonomer.core.main

import cats.implicits.*
import com.monovore.decline.{Command, Opts}

import java.time.LocalDate

class RestonomerCmdArgsParser {

  val restonomer_context_dir_path: Opts[String] = Opts
    .option[String](long = "restonomer_context_dir_path", help = "Full path to restonomer context directory")

  val checkpoint: Opts[String] = Opts
    .option[String](long = "checkpoint", help = "Name of checkpoint to be executed")

  val cmd: Command[(String, String)] =
    Command("Restonomer App", "dumps the data from the REST API provided in checkpoint") {
      (
        restonomer_context_dir_path,
        checkpoint
      ).tupled
    }

  def parse(args: Array[String]): RestonomerRuntimeConf = {
    cmd.parse(args, sys.env) match {
      case Left(help) =>
        System.err.println(help)
        sys.exit(1)

      case Right(parsedValue) =>
        val (
          restonomerContextDirPath,
          checkpointName
        ) = parsedValue

        RestonomerRuntimeConf(restonomerContextDirPath, checkpointName)
    }
  }

}
