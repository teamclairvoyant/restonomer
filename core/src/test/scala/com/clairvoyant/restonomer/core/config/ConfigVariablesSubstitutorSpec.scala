package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.CoreSpec
import java.io.File

class ConfigVariablesSubstitutorSpec extends CoreSpec {

  val configFile = new File(s"$resourcesPath/sample-checkpoint-conf-variable.conf")

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
          |      type = "basic-authentication"
          |      basic-token = ${BASIC_AUTH_TOKEN}
          |    }
          |  }
          |
          |  data-response = {
          |    body-format = "JSON"
          |
          |    persistence = {
          |      type = "file-system"
          |      file-format = "json"
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map[String, String]()

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configFile) shouldBe expectedConfigString
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
          |      type = "basic-authentication"
          |      basic-token = "efgh5678"
          |    }
          |  }
          |
          |  data-response = {
          |    body-format = "JSON"
          |
          |    persistence = {
          |      type = "file-system"
          |      file-format = "json"
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "abcd1234")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configFile) shouldBe expectedConfigString
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
          |      type = "basic-authentication"
          |      basic-token = "abcd1234"
          |    }
          |  }
          |
          |  data-response = {
          |    body-format = "JSON"
          |
          |    persistence = {
          |      type = "file-system"
          |      file-format = "json"
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map[String, String]()
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "abcd1234")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configFile) shouldBe expectedConfigString
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
          |      type = "basic-authentication"
          |      basic-token = "efgh5678"
          |    }
          |  }
          |
          |  data-response = {
          |    body-format = "JSON"
          |
          |    persistence = {
          |      type = "file-system"
          |      file-format = "json"
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map[String, String]()
      val configVariablesFromApplicationArgs = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val environmentVariables = Map[String, String]()

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configFile) shouldBe expectedConfigString
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
          |      type = "basic-authentication"
          |      basic-token = "abcd1234"
          |    }
          |  }
          |
          |  data-response = {
          |    body-format = "JSON"
          |
          |    persistence = {
          |      type = "file-system"
          |      file-format = "json"
          |      file-path = "/tmp"
          |    }
          |  }
          |}
          |""".stripMargin

      val configVariablesFromFile = Map("BASIC_AUTH_TOKEN" -> "abcd1234")
      val configVariablesFromApplicationArgs = Map("BASIC_AUTH_TOKEN" -> "efgh5678")
      val environmentVariables = Map("BASIC_AUTH_TOKEN" -> "ijkl9876")

      ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs, environmentVariables)
        .substituteConfigVariables(configFile) shouldBe expectedConfigString
    }

}
