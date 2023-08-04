package com.clairvoyant.restonomer.core.config

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.model.CheckpointConfig

import java.io.FileNotFoundException

class LocalRestonomerContextLoaderSpec extends CoreSpec with DataScalaxyTestUtil {

  given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

  "fileExists()" should "return true" in {
    val configFilePath = s"$resourcesPath/sample-checkpoint-valid.conf"
    LocalRestonomerContextLoader().fileExists(configFilePath) shouldBe true
  }

  "fileExists()" should "return false" in {
    val configFilePath = s"$resourcesPath/sample-checkpoint-valid-1.conf"
    LocalRestonomerContextLoader().fileExists(configFilePath) shouldBe false
  }

  "readConfigFile()" should "return config file content" in {
    val configFilePath = s"$resourcesPath/sample-checkpoint-valid.conf"

    LocalRestonomerContextLoader()
      .readConfigFile(configFilePath)
      .mkString shouldBe """|name = "sample-checkpoint-valid"
                            |
                            |data = {
                            |  data-request = {
                            |    url = "http://test-domain.com"
                            |  }
                            |
                            |  data-response = {
                            |    body = {
                            |      type = "JSON"
                            |    }
                            |
                            |    persistence = {
                            |      type = "FileSystem"
                            |      file-format = "JSON"
                            |      file-path = "/tmp"
                            |    }
                            |  }
                            |}
                            |""".stripMargin
  }

  "loadConfigFromFile() - with non existing file" should "throw RestonomerException" in {
    val thrown =
      the[FileNotFoundException] thrownBy LocalRestonomerContextLoader().loadConfigFromFile[CheckpointConfig](
        s"$resourcesPath/checkpoint_invalid.conf",
        CheckpointConfig.config
      )

    thrown.getMessage should (include("No such file or directory") or include(
      "The system cannot find the file specified"
    ))
  }

  "loadConfigFromFile() - with existing valid file" should "return populated config object" in {
    LocalRestonomerContextLoader().loadConfigFromFile[CheckpointConfig](
      s"$resourcesPath/sample-checkpoint-valid.conf",
      CheckpointConfig.config
    ) shouldBe a[
      CheckpointConfig
    ]
  }

  "loadConfigsFromDirectory()" should "return list that contain CheckpointConfig objects" in {
    LocalRestonomerContextLoader().loadConfigsFromDirectory[CheckpointConfig](
      s"$resourcesPath/checkpoints/",
      CheckpointConfig.config
    ) shouldBe a[List[
      _
    ]]
  }

  "loadConfigsFromDirectory()" should "return list with size 1" in {
    LocalRestonomerContextLoader().loadConfigsFromDirectory[CheckpointConfig](
      s"$resourcesPath/checkpoints/",
      CheckpointConfig.config
    ) should have size 1
  }

}
