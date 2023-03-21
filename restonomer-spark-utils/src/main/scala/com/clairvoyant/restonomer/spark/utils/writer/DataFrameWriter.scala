package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.DataFrame

trait DataFrameWriter {

  def write(dataFrame: DataFrame): Unit

}
