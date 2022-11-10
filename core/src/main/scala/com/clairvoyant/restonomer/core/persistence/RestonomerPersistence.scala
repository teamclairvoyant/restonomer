package com.clairvoyant.restonomer.core.persistence

import org.apache.spark.sql.DataFrame

sealed trait RestonomerPersistence {

  def persist(restonomerResponseDataFrame: DataFrame): Unit

}

case class FileSystem(
    outputFileFormat: String,
    outputFilePath: String
) extends RestonomerPersistence {

  override def persist(restonomerResponseDataFrame: DataFrame): Unit =
    restonomerResponseDataFrame.write
      .format(outputFileFormat)
      .save(outputFilePath)

}
