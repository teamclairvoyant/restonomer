package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class DataFrameToFileSystemWriter(
    override val sparkSession: SparkSession,
    fileFormat: String,
    filePath: String
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame): Unit =
    dataFrame.write
      .format(fileFormat)
      .mode(SaveMode.Overwrite)
      .save(filePath)

}
