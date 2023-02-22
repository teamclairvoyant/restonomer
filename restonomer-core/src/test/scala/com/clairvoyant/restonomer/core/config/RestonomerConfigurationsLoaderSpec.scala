package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.model.CheckpointConfig

import java.io.FileNotFoundException

class RestonomerConfigurationsLoaderSpec extends CoreSpec {

  implicit val configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor()

  "loadConfigFromFile - with non existing file" should "throw RestonomerException" in {
    val thrown =
      the[FileNotFoundException] thrownBy loadConfigFromFile[CheckpointConfig](
        s"$resourcesPath/checkpoint_invalid.conf"
      )

    thrown.getMessage should include("No such file or directory")
  }

  "loadConfigFromFile - with existing valid file" should "return populated config object" in {
    loadConfigFromFile[CheckpointConfig](s"$resourcesPath/sample-checkpoint-valid.conf") shouldBe a[
      CheckpointConfig
    ]
  }

  "loadConfigsFromDirectory" should "return list that contain CheckpointConfig objects" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") shouldBe a[List[_]]
  }

  "loadConfigsFromDirectory" should "return list with size 1" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") should have size 1
  }

}
