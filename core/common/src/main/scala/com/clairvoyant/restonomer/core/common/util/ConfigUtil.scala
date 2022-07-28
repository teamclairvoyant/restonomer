package com.clairvoyant.restonomer.core.common.util

import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import pureconfig.{ConfigReader, ConfigSource}

import java.io.File
import scala.reflect.ClassTag

object ConfigUtil {

  def loadConfigsFromDirectory[C: ClassTag](configDirectoryPath: String)(implicit reader: ConfigReader[C]): List[C] = {
    if (fileExists(configDirectoryPath))
      new File(configDirectoryPath)
        .listFiles()
        .map(loadConfigFromFile[C])
        .toList
    else
      List.empty
  }

  def loadConfigFromFile[C](configFile: File)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

}
