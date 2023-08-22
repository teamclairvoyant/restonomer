package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.writer.aws.s3.DataFrameToS3BucketWriter
import com.clairvoyant.data.scalaxy.writer.aws.s3.formats.{CSVFileFormat => S3CSVFileFormat, FileFormat => S3FileFormat, JSONFileFormat => S3JSONFileFormat, ParquetFileFormat => S3ParquetFileFormat, XMLFileFormat => S3XMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.aws.s3.instances.*
import com.clairvoyant.data.scalaxy.writer.local.file.DataFrameToLocalFileSystemWriter
import com.clairvoyant.data.scalaxy.writer.local.file.formats.{CSVFileFormat => LocalCSVFileFormat, FileFormat => LocalFileFormat, JSONFileFormat => LocalJSONFileFormat, ParquetFileFormat => LocalParquetFileFormat, XMLFileFormat => LocalXMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.local.file.instances.*
import com.clairvoyant.restonomer.spark.utils.writer.{DataFrameToGCSBucketWriter, DataFrameWriter}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.*

import scala.util.Using

@nameWithLabel
sealed trait RestonomerPersistence:
  def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit

case class LocalFileSystem(
    fileFormat: LocalFileFormat,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    fileFormat match {
      case csvFileFormat: LocalCSVFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write[LocalCSVFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            path = filePath
          )

      case jsonFileFormat: LocalJSONFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write[LocalJSONFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            path = filePath
          )

      case xmlFileFormat: LocalXMLFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write[LocalXMLFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            path = filePath
          )

      case parquetFileFormat: LocalParquetFileFormat =>
        DataFrameToLocalFileSystemWriter
          .write[LocalParquetFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            path = filePath
          )
    }

case class S3Bucket(
    bucketName: String,
    fileFormat: S3FileFormat,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    fileFormat match {
      case csvFileFormat: S3CSVFileFormat =>
        DataFrameToS3BucketWriter
          .write[S3CSVFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case jsonFileFormat: S3JSONFileFormat =>
        DataFrameToS3BucketWriter
          .write[S3JSONFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case xmlFileFormat: S3XMLFileFormat =>
        DataFrameToS3BucketWriter
          .write[S3XMLFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case parquetFileFormat: S3ParquetFileFormat =>
        DataFrameToS3BucketWriter
          .write[S3ParquetFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            bucketName = bucketName,
            path = filePath
          )
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
