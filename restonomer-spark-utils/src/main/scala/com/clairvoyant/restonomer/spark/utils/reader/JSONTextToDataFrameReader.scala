package com.clairvoyant.restonomer.spark.utils.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONTextToDataFrameReader(
    override val sparkSession: SparkSession,
    val text: Seq[String]
) extends DataFrameReader {

  import sparkSession.implicits._

  override def read: DataFrame = sparkSession.read.json(text.toDS())

}
