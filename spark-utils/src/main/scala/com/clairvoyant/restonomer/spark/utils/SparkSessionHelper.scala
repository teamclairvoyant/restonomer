package com.clairvoyant.restonomer.spark.utils

import org.apache.spark.sql.{DataFrame, SparkSession}

trait SparkSessionHelper {

  val sparkSession: SparkSession

  def saveDataFrame(dataFrame: DataFrame, format: String): Unit

}
