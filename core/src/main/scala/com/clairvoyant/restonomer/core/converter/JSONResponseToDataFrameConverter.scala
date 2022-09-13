package com.clairvoyant.restonomer.core.converter

import org.apache.spark.sql.DataFrame

class JSONResponseToDataFrameConverter extends ResponseToDataFrameConverter {

  import sparkSession.implicits._

  override def convertResponseToDataFrame(restonomerResponseBody: String): DataFrame = {
    sparkSession.read
      .json(Seq(restonomerResponseBody).toDS())
  }

}
