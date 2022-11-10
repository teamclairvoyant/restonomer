package com.clairvoyant.restonomer.spark.utils

import org.apache.spark.sql.{DataFrame, SparkSession}

class SparkSessionFileSystemHelper(override val sparkSession: SparkSession, path: String) extends SparkSessionHelper {

  override def saveDataFrame(dataFrame: DataFrame, format: String): Unit =
    dataFrame.write
      .format(format)
      .save(path)

}
