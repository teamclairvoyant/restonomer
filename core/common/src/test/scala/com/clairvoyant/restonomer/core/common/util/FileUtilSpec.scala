package com.clairvoyant.restonomer.core.common.util

import com.clairvoyant.restonomer.core.common.CommonSpec
import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists

class FileUtilSpec extends CommonSpec {

  "fileExists" should "return false" in {
    fileExists(s"$resourcesPath/sample-checkpoint-doesnotexist.conf") shouldBe false
  }

  "fileExists" should "return true" in {
    fileExists(s"$resourcesPath/sample-checkpoint-valid.conf") shouldBe true
  }

}
