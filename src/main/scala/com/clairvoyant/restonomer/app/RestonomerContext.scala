package com.clairvoyant.restonomer.app

import com.clairvoyant.restonomer.config.{ConfigVariablesSubstitutor, GCSRestonomerContextLoader, LocalRestonomerContextLoader, RestonomerContextLoader}
import com.clairvoyant.restonomer.exception.RestonomerException
import com.clairvoyant.restonomer.model.{ApplicationConfig, CheckpointConfig}
import com.google.cloud.storage.{Storage, StorageOptions}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import zio.config.magnolia.*

import java.io.FileNotFoundException

object RestonomerContext {

  def apply(
      restonomerContextDirectoryPath: String,
      configVariablesFromApplicationArgs: Map[String, String] = Map()
  ): RestonomerContext = {
    val restonomerContextLoader =
      if (restonomerContextDirectoryPath.startsWith("gs://")) {
        given gcsStorageClient: Storage = StorageOptions.getDefaultInstance.getService
        GCSRestonomerContextLoader()
      } else
        LocalRestonomerContextLoader()

    if (restonomerContextLoader.fileExists(restonomerContextDirectoryPath))
      new RestonomerContext(restonomerContextLoader, restonomerContextDirectoryPath, configVariablesFromApplicationArgs)
    else
      throw new RestonomerException(
        s"The restonomerContextDirectoryPath: $restonomerContextDirectoryPath does not exists."
      )
  }

}

class RestonomerContext(
    val restonomerContextLoader: RestonomerContextLoader,
    val restonomerContextDirectoryPath: String,
    val configVariablesFromApplicationArgs: Map[String, String]
) {
  // ---------- CONFIG VARIABLES ---------- //

  private val CONFIG_VARIABLES_FILE_PATH = s"$restonomerContextDirectoryPath/uncommitted/config_variables.conf"

  private val configVariablesFromFile = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (restonomerContextLoader.fileExists(CONFIG_VARIABLES_FILE_PATH))
      restonomerContextLoader
        .loadConfigFromFile[Map[String, String]](CONFIG_VARIABLES_FILE_PATH, deriveConfig[Map[String, String]])
    else
      Map[String, String]()
  }

  given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] =
    Some(ConfigVariablesSubstitutor(configVariablesFromFile, configVariablesFromApplicationArgs))

  // ---------- APPLICATION CONFIGS ---------- //

  private val APPLICATION_CONFIG_FILE_PATH = s"$restonomerContextDirectoryPath/application.conf"

  private val applicationConfig = {
    given configVariablesSubstitutor: Option[ConfigVariablesSubstitutor] = None

    if (restonomerContextLoader.fileExists(APPLICATION_CONFIG_FILE_PATH))
      restonomerContextLoader
        .loadConfigFromFile[ApplicationConfig](APPLICATION_CONFIG_FILE_PATH, ApplicationConfig.config)
    else
      throw new FileNotFoundException(
        s"The application config file with the path: $APPLICATION_CONFIG_FILE_PATH does not exists."
      )
  }

  // ---------- RUN CHECKPOINTS ---------- //

  private val sparkConf = applicationConfig.sparkConfigs
    .map { sparkConfigs =>
      sparkConfigs.foldLeft(new SparkConf()) { case (sparkConf, sparkConfig) =>
        sparkConf.set(sparkConfig._1, sparkConfig._2)
      }
    }
    .getOrElse(new SparkConf())

  private def runCheckpoints(checkpointConfigs: List[CheckpointConfig]): Unit =
    checkpointConfigs.foreach { checkpointConfig =>
      println(s"Checkpoint Name -> ${checkpointConfig.name}\n")
      runCheckpoint(checkpointConfig)
      println("\n=====================================================\n")
    }

  private val CHECKPOINTS_CONFIG_DIRECTORY_PATH = s"$restonomerContextDirectoryPath/checkpoints"

  def runCheckpoint(checkpointFilePath: String): Unit = {
    val absoluteCheckpointFilePath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointFilePath"

    if (restonomerContextLoader.fileExists(absoluteCheckpointFilePath))
      runCheckpoint(
        restonomerContextLoader
          .loadConfigFromFile[CheckpointConfig](absoluteCheckpointFilePath, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The checkpoint file with the path: $absoluteCheckpointFilePath does not exists."
      )
  }

  def runCheckpointsUnderDirectory(checkpointsDirectoryPath: String): Unit = {
    val absoluteCheckpointsDirectoryPath = s"$CHECKPOINTS_CONFIG_DIRECTORY_PATH/$checkpointsDirectoryPath"

    if (restonomerContextLoader.fileExists(absoluteCheckpointsDirectoryPath))
      runCheckpoints(
        restonomerContextLoader
          .loadConfigsFromDirectory[CheckpointConfig](absoluteCheckpointsDirectoryPath, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The config directory with the path: $absoluteCheckpointsDirectoryPath does not exists."
      )
  }

  def runAllCheckpoints(): Unit =
    if (restonomerContextLoader.fileExists(CHECKPOINTS_CONFIG_DIRECTORY_PATH))
      runCheckpoints(
        restonomerContextLoader
          .loadConfigsFromDirectory[CheckpointConfig](CHECKPOINTS_CONFIG_DIRECTORY_PATH, CheckpointConfig.config)
      )
    else
      throw new FileNotFoundException(
        s"The config directory with the path: $CHECKPOINTS_CONFIG_DIRECTORY_PATH does not exists."
      )

  def runCheckpoint(checkpointConfig: CheckpointConfig): Unit = {
    given sparkSession: SparkSession =
      SparkSession
        .builder()
        .config {
          checkpointConfig.sparkConfigs
            .foldLeft(sparkConf) { case (sparkConf, sparkConfig) => sparkConf.set(sparkConfig._1, sparkConfig._2) }
        }
        .getOrCreate()

    RestonomerWorkflow.run(checkpointConfig)
  }

}
