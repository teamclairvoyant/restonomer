package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.spark.utils.writer.DataFrameWriter
import org.apache.spark.sql.DataFrame

sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame, dataFrameWriter: DataFrameWriter): Unit =
    dataFrameWriter.write(restonomerResponseDF)

}

case class FileSystem(
    fileFormat: String,
    filePath: String
) extends RestonomerPersistence
