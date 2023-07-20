package com.clairvoyant.restonomer.core.main

object RestonomerApp extends App {

  val cmdLineArgsParser = new RestonomerCmdArgsParser()

  val runtimeConf: RestonomerRuntimeConf = cmdLineArgsParser.parse(args)

}
