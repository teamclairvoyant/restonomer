package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataFrameWriter {

  def write(dataFrame: DataFrame)(using sparkSession: SparkSession): Unit

}
