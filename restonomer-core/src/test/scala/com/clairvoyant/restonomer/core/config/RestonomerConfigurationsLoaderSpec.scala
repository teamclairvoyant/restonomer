package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader.*
import com.clairvoyant.restonomer.core.model.CheckpointConfig

import java.io.FileNotFoundException

class RestonomerConfigurationsLoaderSpec extends CoreSpec {

  given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

  "loadConfigFromFile - with non existing file" should "throw RestonomerException" in {
    val thrown =
      the[FileNotFoundException] thrownBy loadConfigFromFile[CheckpointConfig](
        s"$resourcesPath/checkpoint_invalid.conf",
        CheckpointConfig.config
      )

    thrown.getMessage should include("No such file or directory")
  }

  "loadConfigFromFile - with existing valid file" should "return populated config object" in {
    loadConfigFromFile[CheckpointConfig](
      s"$resourcesPath/sample-checkpoint-valid.conf",
      CheckpointConfig.config
    ) shouldBe a[
      CheckpointConfig
    ]
  }

  "loadConfigsFromDirectory" should "return list that contain CheckpointConfig objects" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/", CheckpointConfig.config) shouldBe a[List[
      _
    ]]
  }

  "loadConfigsFromDirectory" should "return list with size 1" in {
    loadConfigsFromDirectory[CheckpointConfig](
      s"$resourcesPath/checkpoints/",
      CheckpointConfig.config
    ) should have size 1
  }

}
