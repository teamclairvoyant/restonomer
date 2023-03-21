package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.writer.DataFrameWriter
import org.apache.spark.sql.{DataFrame, SaveMode}
import zio.config.derivation._

@nameWithLabel
sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame, dataFrameWriter: DataFrameWriter): Unit =
    dataFrameWriter.write(restonomerResponseDF)

}

case class FileSystem(
    fileFormat: String,
    filePath: String
) extends RestonomerPersistence

case class S3Bucket(
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: SaveMode,
    writeOptions: Map[String, String],
    numberOfPartitions: Option[Int],
    partitionColumns: Seq[String]
) extends RestonomerPersistence
