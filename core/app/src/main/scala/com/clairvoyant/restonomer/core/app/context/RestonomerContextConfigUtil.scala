package com.clairvoyant.restonomer.core.app.context

import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import pureconfig.{ConfigReader, ConfigSource}

import java.io.File
import scala.reflect.ClassTag

object RestonomerContextConfigUtil {

  def readConfigs[C: ClassTag](configDirectoryPath: String)(implicit reader: ConfigReader[C]): List[C] = {
    if (fileExists(configDirectoryPath))
      new File(configDirectoryPath)
        .listFiles()
        .map(loadConfig[C])
        .toList
    else
      List.empty
  }

  def loadConfig[C](configFile: File)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

}
