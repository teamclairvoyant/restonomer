package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame)(implicit sparkSession: SparkSession): Unit

}

case class FileSystem(
    fileFormat: String,
    filePath: String
) extends RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame)(implicit sparkSession: SparkSession): Unit =
    new DataFrameToFileSystemWriter(
      sparkSession = sparkSession,
      fileFormat = fileFormat,
      filePath = filePath
    ).write(restonomerResponseDF)

}
