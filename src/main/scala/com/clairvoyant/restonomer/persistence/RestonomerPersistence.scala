package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.writer.aws.s3.DataFrameToS3BucketWriter
import com.clairvoyant.data.scalaxy.writer.aws.s3.formats.{CSVFileFormat as S3CSVFileFormat, FileFormat as S3FileFormat, JSONFileFormat as S3JSONFileFormat, ParquetFileFormat as S3ParquetFileFormat, XMLFileFormat as S3XMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.aws.s3.instances.*
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.DataFrameToGCSBucketWriter
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.DataFrameToBigQueryWriter
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.types.{BigQueryWriterType, DirectBigQueryWriterType, IndirectBigQueryWriterType}
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.instances.{DataFrameToDirectBQWriter, DataFrameToIndirectBQWriter}
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.formats.{CSVFileFormat as GCSCSVFileFormat, FileFormat as GCSFileFormat, JSONFileFormat as GCSJSONFileFormat, ParquetFileFormat as GCSParquetFileFormat, XMLFileFormat as GCSXMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.instances.*
import com.clairvoyant.data.scalaxy.writer.local.file.DataFrameToLocalFileSystemWriter
import com.clairvoyant.data.scalaxy.writer.local.file.formats.{CSVFileFormat as LocalCSVFileFormat, FileFormat as LocalFileFormat, JSONFileFormat as LocalJSONFileFormat, ParquetFileFormat as LocalParquetFileFormat, XMLFileFormat as LocalXMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.local.file.instances.*
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.nameWithLabel

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
    fileFormat: GCSFileFormat,
    filePath: String,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration

    hadoopConfigurations.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    hadoopConfigurations.set("google.cloud.auth.service.account.enable", "true")
    hadoopConfigurations.set(
      "google.cloud.auth.service.account.json.keyfile",
      serviceAccountCredentialsFile.getOrElse("")
    )

    fileFormat match {
      case csvFileFormat: GCSCSVFileFormat =>
        DataFrameToGCSBucketWriter
          .write[GCSCSVFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case jsonFileFormat: GCSJSONFileFormat =>
        DataFrameToGCSBucketWriter
          .write[GCSJSONFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case xmlFileFormat: GCSXMLFileFormat =>
        DataFrameToGCSBucketWriter
          .write[GCSXMLFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case parquetFileFormat: GCSParquetFileFormat =>
        DataFrameToGCSBucketWriter
          .write[GCSParquetFileFormat](
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            bucketName = bucketName,
            path = filePath
          )
    }

case class BigQuery(
    serviceAccountCredentialsFile: Option[String],
    table: String,
    dataset: Option[String] = None,
    project: Option[String] = None,
    parentProject: Option[String] = None,
    saveMode: String = SaveMode.Overwrite.name(),
    writerType: BigQueryWriterType
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration
    if (serviceAccountCredentialsFile.isDefined) {
      hadoopConfigurations.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
      hadoopConfigurations.set("google.cloud.auth.service.account.enable", "true")
      hadoopConfigurations.set(
        "google.cloud.auth.service.account.json.keyfile",
        serviceAccountCredentialsFile.getOrElse("")
      )
    }

    writerType match {
      case directWriterType: DirectBigQueryWriterType =>
        DataFrameToBigQueryWriter
          .write[DirectBigQueryWriterType](
            dataFrame = restonomerResponseDF,
            table = table,
            dataset = dataset,
            project = project,
            parentProject = parentProject,
            saveMode = SaveMode.valueOf(saveMode),
            writerType = directWriterType
          )
      case indirectWriterType: IndirectBigQueryWriterType =>
        DataFrameToBigQueryWriter
          .write[IndirectBigQueryWriterType](
            dataFrame = restonomerResponseDF,
            table = table,
            dataset = dataset,
            project = project,
            parentProject = parentProject,
            saveMode = SaveMode.valueOf(saveMode),
            writerType = indirectWriterType
          )
    }
