package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.CoreSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.io.File

class ConfigVariablesSubstitutorSpec extends CoreSpec {

  val configFile = new File(s"$resourcesPath/sample-checkpoint-conf-variable.conf")

  "substituteConfigVariables - with empty config variables and empty env variables" should "return same config string" in {
    val expectedConfigString =
      """name = "sample-checkpoint-conf-variable"
        |
        |request = {
        |  url = "http://test-domain.com"
        |}
        |
        |authentication = {
        |  type = "basic-authentication"
        |  basic-token = ${BASIC_AUTH_TOKEN}
        |}
        |""".stripMargin

    val environmentVariables = Map[String, String]()
    val configVariables = Map[String, String]()

    ConfigVariablesSubstitutor(environmentVariables, configVariables).substituteConfigVariables(
      configFile
    ) shouldBe expectedConfigString
  }

  "substituteConfigVariables - with the config variable value present both in env variables and config variables" should
    "return the config string with the config variable being replaced by value in env variable" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |request = {
          |  url = "http://test-domain.com"
          |}
          |
          |authentication = {
          |  type = "basic-authentication"
          |  basic-token = "abcd1234"
          |}
          |""".stripMargin

      val environmentVariables = Map[String, String]("BASIC_AUTH_TOKEN" -> "abcd1234")
      val configVariables = Map[String, String]("BASIC_AUTH_TOKEN" -> "efgh5678")

      ConfigVariablesSubstitutor(environmentVariables, configVariables).substituteConfigVariables(
        configFile
      ) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present only in env variables" should
    "return the config string with the config variable being replaced by value in env variable" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |request = {
          |  url = "http://test-domain.com"
          |}
          |
          |authentication = {
          |  type = "basic-authentication"
          |  basic-token = "abcd1234"
          |}
          |""".stripMargin

      val environmentVariables = Map[String, String]("BASIC_AUTH_TOKEN" -> "abcd1234")
      val configVariables = Map[String, String]("BASIC_AUTH_TOKEN_1" -> "efgh5678")

      ConfigVariablesSubstitutor(environmentVariables, configVariables).substituteConfigVariables(
        configFile
      ) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present only in config variables" should
    "return the config string with the config variable being replaced by value in config variables" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |request = {
          |  url = "http://test-domain.com"
          |}
          |
          |authentication = {
          |  type = "basic-authentication"
          |  basic-token = "efgh5678"
          |}
          |""".stripMargin

      val environmentVariables = Map[String, String]("BASIC_AUTH_TOKEN_1" -> "abcd1234")
      val configVariables = Map[String, String]("BASIC_AUTH_TOKEN" -> "efgh5678")

      ConfigVariablesSubstitutor(environmentVariables, configVariables).substituteConfigVariables(
        configFile
      ) shouldBe expectedConfigString
    }

}
