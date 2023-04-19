package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.spark.utils.reader.CSVTextToDataFrameReader
import org.apache.spark.sql.{DataFrame, SparkSession}

class CSVResponseToDataFrameConverter(
    containsHeader: Boolean = true
) extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(
      restonomerResponseBody: Seq[String]
  )(using sparkSession: SparkSession): DataFrame =
    new CSVTextToDataFrameReader(
      sparkSession = sparkSession,
      containsHeader = containsHeader
    ).read(text = restonomerResponseBody.flatMap(_.split("\n")))

}
