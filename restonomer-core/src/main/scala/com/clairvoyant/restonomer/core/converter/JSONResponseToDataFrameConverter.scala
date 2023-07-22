package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONResponseToDataFrameConverter(dataColumnName: Option[String] = None) extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(
      restonomerResponseBody: Seq[String]
  )(implicit sparkSession: SparkSession): DataFrame = {
    val responseDF =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(restonomerResponseBody)

    dataColumnName
      .map { dataColumn =>
        responseDF
          .select(explode(col(dataColumn)).as("records"))
          .select("records.*")
      }
      .getOrElse(responseDF)
  }

}
