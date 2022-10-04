package com.clairvoyant.restonomer.core.config

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.matching.Regex

class ConfigVariablesSubstitutor(environmentVariables: Map[String, String], configVariables: Map[String, String]) {

  private val CONFIG_VARIABLE_REGEX_PATTERN: Regex = """\$\{(\S*)}""".r

  def substituteConfigVariables(configFile: File): String = {
    @tailrec
    def substituteConfigVariablesHelper(remainingMatchers: List[Regex.Match], configString: String): String = {
      if (remainingMatchers.isEmpty)
        configString
      else {
        val matcher = remainingMatchers.head

        val substituteValue =
          if (environmentVariables.contains(matcher.group(1)))
            s"\"${environmentVariables(matcher.group(1))}\""
          else if (configVariables.contains(matcher.group(1)))
            s"\"${configVariables(matcher.group(1))}\""
          else
            matcher.group(0)

        substituteConfigVariablesHelper(remainingMatchers.tail, configString.replace(matcher.group(0), substituteValue))
      }
    }

    val configFileSource = Source.fromFile(configFile)

    val configString =
      try configFileSource.mkString
      finally configFileSource.close()

    val matchers = CONFIG_VARIABLE_REGEX_PATTERN.findAllMatchIn(configString).toList

    substituteConfigVariablesHelper(matchers, configString)
  }

}

object ConfigVariablesSubstitutor {

  def apply(
      environmentVariables: Map[String, String] = sys.env,
      configVariables: Map[String, String] = Map()
  ): ConfigVariablesSubstitutor = new ConfigVariablesSubstitutor(environmentVariables, configVariables)

}
