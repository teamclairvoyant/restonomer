package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader._
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import pureconfig.generic.auto._

import java.io.FileNotFoundException

class RestonomerConfigurationsLoaderSpec extends CoreSpec {

  "loadConfigFromFile" should "return CheckpointConfig object" in {
    loadConfigFromFile[CheckpointConfig](s"$resourcesPath/sample-checkpoint-valid.conf") shouldBe a[CheckpointConfig]
  }

  "loadConfigFromFile" should "throw RestonomerContextException" in {
    val thrown =
      the[RestonomerContextException] thrownBy loadConfigFromFile[CheckpointConfig](
        s"$resourcesPath/sample-checkpoint-invalid.conf"
      )
    thrown.getMessage should include("Key not found: 'url'")
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

}
