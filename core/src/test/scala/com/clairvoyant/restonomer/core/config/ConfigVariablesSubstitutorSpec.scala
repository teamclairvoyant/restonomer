package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.CoreSpec

import java.io.File

class ConfigVariablesSubstitutorSpec extends CoreSpec {

  val configFile = new File(s"$resourcesPath/checkpoints/sample-checkpoint-conf-variable.conf")

  "substituteConfigVariables - with empty config variables and empty env variables" should "return" in {
    val configVariables = Map[String, String]()
    val environmentVariables = Map[String, String]()

    val configVariablesSubstitutor = new ConfigVariablesSubstitutor(configVariables)
    println(configVariablesSubstitutor.substituteConfigVariables(configFile))
  }

}
