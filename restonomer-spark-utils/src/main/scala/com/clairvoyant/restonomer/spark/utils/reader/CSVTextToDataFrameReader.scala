package com.clairvoyant.restonomer.spark.utils.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class CSVTextToDataFrameReader(
    override val sparkSession: SparkSession,
    containsHeader: Boolean = true
) extends DataFrameReader {

  import sparkSession.implicits._

  override def read(text: Seq[String]): DataFrame =
    sparkSession.read
      .option("header", containsHeader.toString)
      .csv(text.toDS())

}
