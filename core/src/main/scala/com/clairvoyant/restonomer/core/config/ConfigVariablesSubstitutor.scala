package com.clairvoyant.restonomer.core.config

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.matching.Regex

class ConfigVariablesSubstitutor(configVariables: Map[String, String]) {

  val CONFIG_VARIABLE_PATTERN: Regex = """\$\{(\S*)}""".r

  def substituteConfigVariables(configFile: File): String = {
    @tailrec
    def substituteConfigVariablesHelper(remainingMatchers: List[Regex.Match], configString: String): String = {
      if (remainingMatchers.isEmpty)
        configString
      else {
        val matcher = remainingMatchers.head
        val newConfigString = configString.replace(matcher.group(0), s"\"${configVariables(matcher.group(1))}\"")

        substituteConfigVariablesHelper(remainingMatchers.tail, newConfigString)
      }
    }

    val configFileSource = Source.fromFile(configFile)

    val configString =
      try configFileSource.mkString
      finally configFileSource.close()

    val matchers = CONFIG_VARIABLE_PATTERN.findAllMatchIn(configString).toList

    substituteConfigVariablesHelper(matchers, configString)
  }

}

object ConfigVariablesSubstitutor {

  def apply(configVariables: Map[String, String] = Map()): ConfigVariablesSubstitutor =
    new ConfigVariablesSubstitutor(configVariables)

}
