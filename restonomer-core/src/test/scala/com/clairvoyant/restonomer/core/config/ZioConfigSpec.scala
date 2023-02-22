package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.config.RestonomerConfigurationsLoader.loadConfigFromFile
import com.clairvoyant.restonomer.core.model.CheckpointConfig

class ZioConfigSpec extends CoreSpec {
  implicit val configVariablesSubstitutor: ConfigVariablesSubstitutor = ConfigVariablesSubstitutor()

  "loadConfigFromFile - " should "return list with size 1" in {
    val rp = loadConfigFromFile[CheckpointConfig](s"$resourcesPath/restonomer-persistence-1.conf")
  }

}
