package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONResponseToDataFrameConverter extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(restonomerResponseBody: String)(implicit sparkSession: SparkSession): DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession,
      text = restonomerResponseBody
    ).read

}
