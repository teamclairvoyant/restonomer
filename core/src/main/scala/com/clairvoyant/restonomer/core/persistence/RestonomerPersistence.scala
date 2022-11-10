package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.SparkSessionFileSystemHelper
import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame)(implicit sparkSession: SparkSession): Unit

}

case class FileSystem(
    outputFileFormat: String,
    outputFilePath: String
) extends RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame)(implicit sparkSession: SparkSession): Unit =
    new SparkSessionFileSystemHelper(
      sparkSession = sparkSession,
      path = outputFilePath
    ).saveDataFrame(
      dataFrame = restonomerResponseDF,
      format = outputFileFormat
    )

}
