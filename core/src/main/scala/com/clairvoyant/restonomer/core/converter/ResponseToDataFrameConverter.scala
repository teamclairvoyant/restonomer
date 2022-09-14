package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.core.common.ResponseBodyFormatTypes
import com.clairvoyant.restonomer.core.common.ResponseBodyFormatTypes.JSON
import org.apache.spark.sql.{DataFrame, SparkSession}

trait ResponseToDataFrameConverter {

  def convertResponseToDataFrame(restonomerResponseBody: String)(implicit sparkSession: SparkSession): DataFrame

}

object ResponseToDataFrameConverter {

  def apply(responseBodyFormat: String): ResponseToDataFrameConverter = {
    ResponseBodyFormatTypes(responseBodyFormat) match {
      case JSON =>
        new JSONResponseToDataFrameConverter()
    }
  }

}
