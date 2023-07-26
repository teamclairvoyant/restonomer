package com.clairvoyant.restonomer.core.config

import com.clairvoyant.restonomer.core.exception.RestonomerException
import zio.*
import zio.config.typesafe.*

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

trait RestonomerContextLoader {

  def fileExists(filePath: String): Boolean

  def readConfigFile(configFilePath: String): Source

  def loadConfigFromFile[C](configFilePath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): C =
    Using(readConfigFile(configFilePath)) { configFileSource =>
      Unsafe.unsafe(implicit u => {
        zio.Runtime.default.unsafe
          .run(
            ConfigProvider
              .fromHoconString(
                configVariablesSubstitutor
                  .map(_.substituteConfigVariables(configFileSource.mkString))
                  .getOrElse(configFileSource.mkString)
              )
              .load(config)
          )
          .getOrThrowFiberFailure()
      })
    } match {
      case Success(config) =>
        config
      case Failure(exception) =>
        throw exception
    }

  def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C]

}
