package com.clairvoyant.restonomer.core.converter

import org.apache.spark.sql.{DataFrame, SparkSession}

trait ResponseToDataFrameConverter {

  def convertResponseToDataFrame(restonomerResponseBody: Seq[String])(implicit sparkSession: SparkSession): DataFrame

}
