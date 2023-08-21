package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.writer.local.file.DataFrameToLocalFileSystemWriter
import com.clairvoyant.data.scalaxy.writer.local.file.formats.*
import com.clairvoyant.data.scalaxy.writer.local.file.instances.*
import com.clairvoyant.restonomer.spark.utils.writer.{DataFrameToGCSBucketWriter, DataFrameToS3BucketWriter, DataFrameWriter}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.*

import scala.util.Using

@nameWithLabel
sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit

}

case class LocalFileSystem(
    fileFormat: FileFormat,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    fileFormat match {
      case csvFileFormat: CSVFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            path = filePath
          )

      case jsonFileFormat: JSONFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            path = filePath
          )

      case xmlFileFormat: XMLFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            path = filePath
          )

      case parquetFileFormat: ParquetFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            path = filePath
          )
    }

case class S3Bucket(
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit = {
    DataFrameToS3BucketWriter(
      bucketName = bucketName,
      fileFormat = fileFormat,
      filePath = filePath,
      saveMode = saveMode
    ).write(restonomerResponseDF)
  }

case class GCSBucket(
    serviceAccountCredentialsFile: Option[String],
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    DataFrameToGCSBucketWriter(
      serviceAccountCredentialsFile = serviceAccountCredentialsFile,
      bucketName = bucketName,
      fileFormat = fileFormat,
      filePath = filePath,
      saveMode = saveMode
    ).write(restonomerResponseDF)
