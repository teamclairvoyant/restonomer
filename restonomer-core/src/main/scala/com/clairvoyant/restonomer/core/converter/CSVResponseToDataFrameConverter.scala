package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.spark.utils.reader.CSVTextToDataFrameReader
import org.apache.spark.sql.{DataFrame, SparkSession}

class CSVResponseToDataFrameConverter extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(
      restonomerResponseBody: Seq[String]
  )(implicit sparkSession: SparkSession): DataFrame =
    new CSVTextToDataFrameReader(
      sparkSession = sparkSession,
      text = restonomerResponseBody.mkString
    ).read

}
