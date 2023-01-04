package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.util.FileUtil.fileExists

class FileUtilSpec extends CoreSpec {

  "fileExists" should "return false" in {
    fileExists(s"$resourcesPath/sample-checkpoint-doesnotexist.conf") shouldBe false
  }

  "fileExists" should "return true" in {
    fileExists(s"$resourcesPath/sample-checkpoint-valid.conf") shouldBe true
  }

}
