package com.clairvoyant.restonomer.spark.utils.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataFrameReader {

  val sparkSession: SparkSession

  def read: DataFrame

}
