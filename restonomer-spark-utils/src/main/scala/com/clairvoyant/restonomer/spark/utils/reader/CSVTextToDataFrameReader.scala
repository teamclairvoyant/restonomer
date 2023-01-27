package com.clairvoyant.restonomer.spark.utils.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class CSVTextToDataFrameReader(
    override val sparkSession: SparkSession,
    val text: String
) extends DataFrameReader {

  import sparkSession.implicits._

  override def read: DataFrame =
    sparkSession.read.option("header", "true").option("sep", ",").csv(text.split("\\n").toSeq.toDS())

}
