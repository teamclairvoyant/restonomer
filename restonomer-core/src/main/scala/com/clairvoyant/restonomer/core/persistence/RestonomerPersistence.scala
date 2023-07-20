package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.writer.DataFrameWriter
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.*

import scala.util.Using

@nameWithLabel
sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame, dataFrameWriter: DataFrameWriter)(
      using sparkSession: SparkSession
  ): Unit = dataFrameWriter.write(restonomerResponseDF)

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

case class GCSBucket(
    serviceAccountCredentialsFile: Option[String],
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence
