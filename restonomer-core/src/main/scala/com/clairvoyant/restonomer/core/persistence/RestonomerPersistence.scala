package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.writer.DataFrameWriter
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.*

import scala.util.Using

@nameWithLabel
sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame, dataFrameWriter: DataFrameWriter): Unit =
    dataFrameWriter.write(restonomerResponseDF)
}

case class FileSystem(
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence

case class S3Bucket(
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence

case class GcsBucket(
    serviceAccountCredFile: String,
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence