package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import pureconfig.generic.auto._

import java.io.FileNotFoundException

class RestonomerConfigurationsLoaderSpec extends CoreSpec {

  "loadConfigVariables - with empty config variable file" should "return empty map" in {
    loadConfigVariables(s"$resourcesPath/uncommitted/config_variables_empty.conf") should have size 0
  }

  "loadConfigVariables - with non existing config variable file" should "return empty map" in {
    loadConfigVariables(s"$resourcesPath/uncommitted/config_variables_invalid.conf") should have size 0
  }

  "loadConfigVariables - with existing valid config variable file" should "return non empty map" in {
    loadConfigVariables(s"$resourcesPath/uncommitted/config_variables.conf") should have size 2
  }

  "loadApplicationConfig - with non existing file" should "throw RestonomerException" in {
    val thrown = the[FileNotFoundException] thrownBy loadApplicationConfig(s"$resourcesPath/application_invalid.conf")

    thrown.getMessage should include(
      "The application config file with the path: core/src/test/resources/application_invalid.conf does not exists."
    )
  }

  "loadApplicationConfig - with existing valid file" should "return populated ApplicationConfig object" in {
    loadApplicationConfig(s"$resourcesPath/application.conf") shouldBe a[ApplicationConfig]
  }

  "loadConfigsFromFilePath - with non existing file" should "throw RestonomerException" in {
    val thrown =
      the[FileNotFoundException] thrownBy loadConfigsFromFilePath[CheckpointConfig](
        s"$resourcesPath/checkpoint_invalid.conf"
      )

    thrown.getMessage should include("No such file or directory")
  }

  "loadConfigsFromFilePath - with existing valid file" should "return populated config object" in {
    loadConfigsFromFilePath[CheckpointConfig](s"$resourcesPath/sample-checkpoint-valid.conf") shouldBe a[
      CheckpointConfig
    ]
  }

  "loadConfigsFromDirectory" should "return list that contain CheckpointConfig objects" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") shouldBe a[List[_]]
  }

  "loadConfigsFromDirectory" should "throw FileNotFoundException" in {
    a[FileNotFoundException] should be thrownBy loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/abcd/")
  }

  "loadConfigsFromDirectory" should "return list with size 1" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") should have size 1
  }

  "loadConfigFromString - with valid config string" should "return valid CheckpointConfig object" in {
    val configString =
      """name = "sample-checkpoint-valid"
        |
        |request = {
        |  name = "request_no_authentication"
        |  url = "http://test-domain.com"
        |}
        |
        |response = {
        |  body = {
        |    format = "JSON"
        |  }
        |}""".stripMargin

    loadConfigFromString[CheckpointConfig](configString) shouldBe a[CheckpointConfig]
    noException should be thrownBy loadConfigFromString[CheckpointConfig](configString)
  }

  "loadConfigFromString - with invalid config string" should "throw Exception" in {
    val configString =
      """name = "sample-checkpoint-invalid"
        |
        |request = {}
        |""".stripMargin

    val thrown = the[RestonomerException] thrownBy loadConfigFromString[CheckpointConfig](configString)

    thrown.getMessage should include("Key not found: 'url'")
  }

}
