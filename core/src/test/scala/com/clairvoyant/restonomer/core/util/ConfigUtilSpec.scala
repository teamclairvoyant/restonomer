package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import com.clairvoyant.restonomer.core.model.CheckpointConfig
import com.clairvoyant.restonomer.core.util.ConfigUtil._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import pureconfig.generic.auto._

import java.io.{File, FileNotFoundException}

class ConfigUtilSpec extends CoreSpec {

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

  "loadConfigsFromDirectory" should "throw FileNotFoundException" in {
    a[FileNotFoundException] should be thrownBy loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/abcd/")
  }

  "loadConfigsFromDirectory" should "return list with size 1" in {
    loadConfigsFromDirectory[CheckpointConfig](s"$resourcesPath/checkpoints/") should have size 1
  }

}
