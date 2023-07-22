package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataFrameWriter {

  def write(dataFrame: DataFrame)(implicit sparkSession: SparkSession): Unit

}
