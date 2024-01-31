package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.writer.aws.redshift.{DataFrameToRedshiftWriter, RedshiftWriterOptions}
import com.clairvoyant.data.scalaxy.writer.aws.s3.DataFrameToS3BucketWriter
import com.clairvoyant.data.scalaxy.writer.aws.s3.formats.{CSVFileFormat as S3CSVFileFormat, FileFormat as S3FileFormat, JSONFileFormat as S3JSONFileFormat, ParquetFileFormat as S3ParquetFileFormat, XMLFileFormat as S3XMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.aws.s3.instances.*
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.DataFrameToBigQueryWriter
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.instances.*
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.types.*
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.DataFrameToGCSBucketWriter
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.formats.{CSVFileFormat as GCSCSVFileFormat, FileFormat as GCSFileFormat, JSONFileFormat as GCSJSONFileFormat, ParquetFileFormat as GCSParquetFileFormat, XMLFileFormat as GCSXMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.gcp.gcs.instances.*
import com.clairvoyant.data.scalaxy.writer.local.file.DataFrameToLocalFileSystemWriter
import com.clairvoyant.data.scalaxy.writer.local.file.formats.{CSVFileFormat as LocalCSVFileFormat, FileFormat as LocalFileFormat, JSONFileFormat as LocalJSONFileFormat, ParquetFileFormat as LocalParquetFileFormat, XMLFileFormat as LocalXMLFileFormat}
import com.clairvoyant.data.scalaxy.writer.local.file.instances.*
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import zio.config.derivation.nameWithLabel

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
        DataFrameToLocalFileSystemWriter[LocalCSVFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            path = filePath
          )

      case jsonFileFormat: LocalJSONFileFormat =>
        DataFrameToLocalFileSystemWriter[LocalJSONFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            path = filePath
          )

      case xmlFileFormat: LocalXMLFileFormat =>
        DataFrameToLocalFileSystemWriter[LocalXMLFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            path = filePath
          )

      case parquetFileFormat: LocalParquetFileFormat =>
        DataFrameToLocalFileSystemWriter[LocalParquetFileFormat]
          .write(
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
        DataFrameToS3BucketWriter[S3CSVFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case jsonFileFormat: S3JSONFileFormat =>
        DataFrameToS3BucketWriter[S3JSONFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case xmlFileFormat: S3XMLFileFormat =>
        DataFrameToS3BucketWriter[S3XMLFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case parquetFileFormat: S3ParquetFileFormat =>
        DataFrameToS3BucketWriter[S3ParquetFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            bucketName = bucketName,
            path = filePath
          )
    }

case class GCSBucket(
    serviceAccountCredentialsFile: Option[String] = None,
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
        DataFrameToGCSBucketWriter[GCSCSVFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = csvFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case jsonFileFormat: GCSJSONFileFormat =>
        DataFrameToGCSBucketWriter[GCSJSONFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = jsonFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case xmlFileFormat: GCSXMLFileFormat =>
        DataFrameToGCSBucketWriter[GCSXMLFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = xmlFileFormat,
            bucketName = bucketName,
            path = filePath
          )

      case parquetFileFormat: GCSParquetFileFormat =>
        DataFrameToGCSBucketWriter[GCSParquetFileFormat]
          .write(
            dataFrame = restonomerResponseDF,
            fileFormat = parquetFileFormat,
            bucketName = bucketName,
            path = filePath
          )
    }

case class BigQuery(
    writerType: BigQueryWriterType,
    serviceAccountCredentialsFile: Option[String] = None,
    table: String,
    dataset: Option[String] = None,
    project: Option[String] = None,
    parentProject: Option[String] = None,
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    val hadoopConfigurations = sparkSession.sparkContext.hadoopConfiguration

    if (serviceAccountCredentialsFile.isDefined) {
      hadoopConfigurations.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
      hadoopConfigurations.set("google.cloud.auth.service.account.enable", "true")
      hadoopConfigurations.set(
        "google.cloud.auth.service.account.json.keyfile",
        serviceAccountCredentialsFile.getOrElse("")
      )
    }

    writerType match {
      case directBigQueryWriterType: DirectBigQueryWriterType =>
        DataFrameToBigQueryWriter[DirectBigQueryWriterType]
          .write(
            dataFrame = restonomerResponseDF,
            table = table,
            dataset = dataset,
            project = project,
            parentProject = parentProject,
            saveMode = SaveMode.valueOf(saveMode),
            writerType = directBigQueryWriterType
          )

      case indirectBigQueryWriterType: IndirectBigQueryWriterType =>
        DataFrameToBigQueryWriter[IndirectBigQueryWriterType]
          .write(
            dataFrame = restonomerResponseDF,
            table = table,
            dataset = dataset,
            project = project,
            parentProject = parentProject,
            saveMode = SaveMode.valueOf(saveMode),
            writerType = indirectBigQueryWriterType
          )
    }

case class Redshift(
    hostName: String,
    port: Int = 5439,
    databaseName: String,
    tableName: String,
    userName: String,
    password: String,
    tempDirPath: String,
    writerOptions: RedshiftWriterOptions = RedshiftWriterOptions(),
    saveMode: String = SaveMode.ErrorIfExists.name()
) extends RestonomerPersistence:

  override def persist(restonomerResponseDF: DataFrame)(using sparkSession: SparkSession): Unit =
    DataFrameToRedshiftWriter.write(
      dataFrame = restonomerResponseDF,
      hostName = hostName,
      port = port,
      databaseName = databaseName,
      tableName = tableName,
      userName = userName,
      password = password,
      tempDirS3Path = tempDirPath,
      writerOptions = writerOptions,
      saveMode = SaveMode.valueOf(saveMode)
    )
