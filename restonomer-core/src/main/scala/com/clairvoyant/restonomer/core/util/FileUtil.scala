package com.clairvoyant.restonomer.core.util

import java.nio.file.{Files, Paths}

object FileUtil {

  def fileExists(filePath: String): Boolean = Files.exists(Paths.get(filePath))

}
