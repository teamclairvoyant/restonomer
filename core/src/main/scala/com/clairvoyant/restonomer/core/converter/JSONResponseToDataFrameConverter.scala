package com.clairvoyant.restonomer.core.converter

import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONResponseToDataFrameConverter extends ResponseToDataFrameConverter {

  override def convertResponseToDataFrame(
      restonomerResponseBody: String
  )(implicit sparkSession: SparkSession): DataFrame = {
    import sparkSession.implicits._

    sparkSession.read
      .json(Seq(restonomerResponseBody).toDS())
  }

}
