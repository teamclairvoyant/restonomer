package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataFrameWriter {

  val sparkSession: SparkSession

  def write(dataFrame: DataFrame): Unit

}
