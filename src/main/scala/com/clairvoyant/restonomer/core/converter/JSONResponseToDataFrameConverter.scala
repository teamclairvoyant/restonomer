package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.data.scalaxy.reader.text.JSONTextToDataFrameReader
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

class JSONResponseToDataFrameConverter(
    columnNameOfCorruptRecord: String,
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
      columnNameOfCorruptRecord = columnNameOfCorruptRecord,
      dateFormat = dateFormat,
      inferSchema = inferSchema,
      locale = locale,
      multiLine = multiLine,
      originalSchema = originalSchema.map(StructType.fromDDL),
      primitivesAsString = primitivesAsString,
      timestampFormat = timestampFormat,
      timestampNTZFormat = timestampNTZFormat
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
