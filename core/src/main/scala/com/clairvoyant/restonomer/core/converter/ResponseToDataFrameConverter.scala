package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.core.common.ResponseBodyFormatTypes
import com.clairvoyant.restonomer.core.common.ResponseBodyFormatTypes.JSON
import org.apache.spark.sql.{DataFrame, SparkSession}

trait ResponseToDataFrameConverter {

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()

  def convertResponseToDataFrame(restonomerResponseBody: String): DataFrame

}

object ResponseToDataFrameConverter {

  def apply(responseBodyFormat: String): ResponseToDataFrameConverter = {
    ResponseBodyFormatTypes(responseBodyFormat) match {
      case JSON =>
        new JSONResponseToDataFrameConverter()
    }
  }

}
