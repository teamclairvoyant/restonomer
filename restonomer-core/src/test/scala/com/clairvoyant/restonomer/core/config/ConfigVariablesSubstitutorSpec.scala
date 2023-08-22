package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.CoreSpec


class ConfigVariablesSubstitutorSpec extends CoreSpec {

  val configString: String =
    """name = "sample-checkpoint-conf-variable"
      |
      |data = {
      |  data-request = {
      |    url = "http://test-domain.com"
      |
      |    authentication = {
      |      type = "BasicAuthentication"
      |      basic-token = ${BASIC_AUTH_TOKEN}
      |    }
      |  }
      |
      |  data-response = {
      |    body = {
      |      type = "JSON"
      |    }
      |
      |    persistence = {
      |      type = "LocalFileSystem"
      |      file-format = {
        type = "JSONFileFormat"
      }
      |      file-path = "/tmp"
      |    }
      |  }
      |}
      |""".stripMargin

  "substituteConfigVariables - with empty configVariablesFromFile, empty configVariablesFromApplicationArgs and " +
    "empty env variables" should "return same config string" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |data = {
          |  data-request = {
          |    url = "http://test-domain.com"
          |
          |    authentication = {
          |      type = "BasicAuthentication"
          |      basic-token = ${BASIC_AUTH_TOKEN}
          |    }
          |  }
          |
          |  data-response = {
          |    body = {
          |      type = "JSON"
          |    }
          |
          |    persistence = {
          |      type = "LocalFileSystem"
          |      file-format = {
        type = "JSONFileFormat"
      }
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map[String, String]()

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configString) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present both in env variables and configVariablesFromFile" should
    "return the config string with the config variable being replaced by value in configVariablesFromFile" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |data = {
          |  data-request = {
          |    url = "http://test-domain.com"
          |
          |    authentication = {
          |      type = "BasicAuthentication"
          |      basic-token = "efgh5678"
          |    }
          |  }
          |
          |  data-response = {
          |    body = {
          |      type = "JSON"
          |    }
          |
          |    persistence = {
          |      type = "LocalFileSystem"
          |      file-format = {
        type = "JSONFileFormat"
      }
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "abcd1234")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configString) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present only in env variables" should
    "return the config string with the config variable being replaced by value in env variable" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |data = {
          |  data-request = {
          |    url = "http://test-domain.com"
          |
          |    authentication = {
          |      type = "BasicAuthentication"
          |      basic-token = "abcd1234"
          |    }
          |  }
          |
          |  data-response = {
          |    body = {
          |      type = "JSON"
          |    }
          |
          |    persistence = {
          |      type = "LocalFileSystem"
          |      file-format = {
        type = "JSONFileFormat"
      }
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "abcd1234")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configString) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present only in configVariablesFromApplicationArgs" should
    "return the config string with the config variable being replaced by value in configVariablesFromApplicationArgs" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |data = {
          |  data-request = {
          |    url = "http://test-domain.com"
          |
          |    authentication = {
          |      type = "BasicAuthentication"
          |      basic-token = "efgh5678"
          |    }
          |  }
          |
          |  data-response = {
          |    body = {
          |      type = "JSON"
          |    }
          |
          |    persistence = {
          |      type = "LocalFileSystem"
          |      file-format = {
        type = "JSONFileFormat"
      }
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val environmentVariables = Map[String, String]()

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configString) shouldBe expectedConfigString
    }

  "substituteConfigVariables - with the config variable value present in all " +
    "configVariablesFromFile, configVariablesFromApplicationArgs and environmentVariables" should
    "return the config string with the config variable being replaced by value in configVariablesFromFile" in {
      val expectedConfigString =
        """name = "sample-checkpoint-conf-variable"
          |
          |data = {
          |  data-request = {
          |    url = "http://test-domain.com"
          |
          |    authentication = {
          |      type = "BasicAuthentication"
          |      basic-token = "abcd1234"
          |    }
          |  }
          |
          |  data-response = {
          |    body = {
          |      type = "JSON"
          |    }
          |
          |    persistence = {
          |      type = "LocalFileSystem"
          |      file-format = {
        type = "JSONFileFormat"
      }
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map("BASIC_AUTH_TOKEN" -> "abcd1234")
      val configVariablesFromApplicationArgs = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "ijkl9876")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configString) shouldBe expectedConfigString
    }

}
