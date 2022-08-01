package com.clairvoyant.restonomer.core.common.util

import com.clairvoyant.restonomer.core.common.CommonSpec
import com.clairvoyant.restonomer.core.common.util.ConfigUtil.{loadConfigFromFile, loadConfigsFromDirectory}
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.config.CheckpointConfig
import pureconfig.generic.auto._

import java.io.File

class ConfigUtilSpec extends CommonSpec {

  "loadConfigFromFile" should "return CheckpointConfig object" in {
    val checkpointFile = new File(s"$resourcesPath/sample-checkpoint-valid.conf")
    loadConfigFromFile[CheckpointConfig](checkpointFile) shouldBe a[CheckpointConfig]
  }

  "loadConfigFromFile" should "throw RestonomerContextException" in {
    val checkpointFile = new File(s"$resourcesPath/sample-checkpoint-invalid.conf")
    val thrown = the[RestonomerContextException] thrownBy loadConfigFromFile[CheckpointConfig](checkpointFile)
    thrown.getMessage should include("Key not found: 'url'")
  }

  "loadConfigsFromDirectory" should "return list that contain CheckpointConfig objects" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") shouldBe a[List[CheckpointConfig]]
  }

  "loadConfigsFromDirectory" should "return empty list" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/abcd/") should have size 0
  }

  "loadConfigsFromDirectory" should "return list with size 1" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") should have size 1
  }

}
