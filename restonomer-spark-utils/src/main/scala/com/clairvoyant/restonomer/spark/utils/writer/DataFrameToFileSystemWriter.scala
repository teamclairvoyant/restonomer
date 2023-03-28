package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SaveMode}

class DataFrameToFileSystemWriter(
    fileFormat: String,
    filePath: String,
    saveMode: String
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame): Unit =
    dataFrame.write
      .format(fileFormat)
      .mode(saveMode)
      .save(filePath)

}
