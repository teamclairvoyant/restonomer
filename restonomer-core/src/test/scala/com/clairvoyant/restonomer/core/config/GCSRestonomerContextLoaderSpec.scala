package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.{CoreSpec, GCSMockSpec}
import com.clairvoyant.restonomer.core.model.CheckpointConfig

class GCSRestonomerContextLoaderSpec extends CoreSpec with GCSMockSpec {

  "fileExists()" should "return true" in {
    val configFilePath =
      "gs://test-bucket/restonomer_context/checkpoints/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie.conf"

    GCSRestonomerContextLoader().fileExists(configFilePath) shouldBe true
  }

  "fileExists()" should "return false" in {
    val configFilePath =
      "gs://test-bucket/restonomer_context/checkpoints/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie-1.conf"

    GCSRestonomerContextLoader().fileExists(configFilePath) shouldBe false
  }

  "readConfigFile()" should "return config file content" in {
    val configFilePath =
      "gs://test-bucket/restonomer_context/checkpoints/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie.conf"

    GCSRestonomerContextLoader()
      .readConfigFile(configFilePath)
      .mkString shouldBe """|name = "checkpoint_api_key_authentication_cookie"
                            |
                            |data = {
                            |  data-request = {
                            |    url = "http://localhost:8080/api-key-auth-cookie"
                            |
                            |    authentication = {
                            |      type = "APIKeyAuthentication"
                            |      api-key-name = "test_api_key_name"
                            |      api-key-value = "test_api_key_value"
                            |      placeholder = "Cookie"
                            |    }
                            |  }
                            |
                            |  data-response = {
                            |    body = {
                            |      type = "JSON"
                            |    }
                            |
                            |    persistence = {
                            |      type = "FileSystem"
                            |      file-format = "parquet"
                            |      file-path = "/tmp/authentication/api_key_authentication"
                            |    }
                            |  }
                            |}
                            |""".stripMargin
  }

  "loadConfigFromFile()" should "return valid config object" in {
    val configFilePath =
      "gs://test-bucket/restonomer_context/checkpoints/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie.conf"

    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    val checkpointConfig = GCSRestonomerContextLoader()
      .loadConfigFromFile[CheckpointConfig](configFilePath, CheckpointConfig.config)

    checkpointConfig shouldBe a[CheckpointConfig]
    checkpointConfig.name shouldBe "checkpoint_api_key_authentication_cookie"
  }

  "loadConfigsFromDirectory()" should "load all configs from directory" in {
    val configDirectoryPath = "gs://test-bucket/restonomer_context/checkpoints/authentication"

    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    val checkpointConfigs = GCSRestonomerContextLoader()
      .loadConfigsFromDirectory[CheckpointConfig](configDirectoryPath, CheckpointConfig.config)

    checkpointConfigs should have size 4
  }

}
