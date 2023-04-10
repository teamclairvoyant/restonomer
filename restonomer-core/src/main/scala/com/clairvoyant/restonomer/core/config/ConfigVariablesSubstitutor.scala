package com.clairvoyant.restonomer.core.config

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.matching.Regex

class ConfigVariablesSubstitutor(
    configVariablesFromFile: Map[String, String],
    configVariablesFromApplicationArgs: Map[String, String],
    environmentVariables: Map[String, String]
) {
  private val CONFIG_VARIABLE_REGEX_PATTERN: Regex = """\$\{(\S*)}""".r

  def substituteConfigVariables(configString: String): String = {
    @tailrec
    def substituteConfigVariablesHelper(remainingMatchers: List[Regex.Match], configString: String): String = {
      if (remainingMatchers.isEmpty) configString
      else {
        val matcher = remainingMatchers.head

        val substituteValue =
          if (configVariablesFromFile.contains(matcher.group(1)))
            s"\"${configVariablesFromFile(matcher.group(1))}\""
          else if (configVariablesFromApplicationArgs.contains(matcher.group(1)))
            s"\"${configVariablesFromApplicationArgs(matcher.group(1))}\""
          else if (environmentVariables.contains(matcher.group(1)))
            s"\"${environmentVariables(matcher.group(1))}\""
          else
            matcher.group(0)

        substituteConfigVariablesHelper(
          remainingMatchers = remainingMatchers.tail,
          configString = configString.replace(matcher.group(0), substituteValue)
        )
      }
    }

    substituteConfigVariablesHelper(
      remainingMatchers = CONFIG_VARIABLE_REGEX_PATTERN.findAllMatchIn(configString).toList,
      configString = configString
    )
  }

}

object ConfigVariablesSubstitutor {

  def apply(
      configVariablesFromFile: Map[String, String] = Map(),
      configVariablesFromApplicationArgs: Map[String, String] = Map(),
      environmentVariables: Map[String, String] = sys.env
  ): ConfigVariablesSubstitutor =
    new ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)

}
