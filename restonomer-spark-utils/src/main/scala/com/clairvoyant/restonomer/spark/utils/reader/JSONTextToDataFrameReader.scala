package com.clairvoyant.restonomer.spark.utils.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONTextToDataFrameReader(
    override val sparkSession: SparkSession
) extends DataFrameReader {

  import sparkSession.implicits.*

  override def read(text: Seq[String]): DataFrame = sparkSession.read.json(text.toDS())

}
