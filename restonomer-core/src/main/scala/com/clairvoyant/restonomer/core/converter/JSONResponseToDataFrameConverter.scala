package com.clairvoyant.restonomer.core.converter

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.types.StructType
import com.clairvoyant.data.scalaxy.reader.text.JSONTextToDataFrameReader

class JSONResponseToDataFrameConverter(
    dataColumnName: Option[String],
    dateFormat: String,
    inferSchema: Boolean,
    locale: String,
    multiLine: Boolean,
    originalSchema: Option[String],
    primitivesAsString: Boolean,
    timestampFormat: String,
    timestampNTZFormat: String
) extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(
      restonomerResponseBody: Seq[String]
  )(using sparkSession: SparkSession): DataFrame = {
    val responseDF = JSONTextToDataFrameReader(
      dateFormat = dateFormat,
      inferSchema = inferSchema,
      locale = locale,
      multiLine = multiLine,
      originalSchema = originalSchema.map(StructType.fromDDL(_)),
      primitivesAsString = primitivesAsString,
      timestampFormat = timestampFormat,
      timestampNTZFormat = timestampNTZFormat
    )

    dataColumnName
      .map { dataColumn =>
        responseDF
          .select(explode(col(dataColumn)).as("records"))
          .select("records.*")
      }
      .getOrElse(responseDF)
  }

}
