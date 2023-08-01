package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.data.scalaxy.reader.text.CSVTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

class CSVResponseToDataFrameConverter(
    columnNameOfCorruptRecord: String,
    dateFormat: String,
    emptyValue: String,
    enforceSchema: Boolean,
    escape: String,
    header: Boolean,
    inferSchema: Boolean,
    ignoreLeadingWhiteSpace: Boolean,
    ignoreTrailingWhiteSpace: Boolean,
    lineSep: String,
    locale: String,
    multiLine: Boolean,
    nanValue: String,
    nullValue: String,
    originalSchema: Option[String],
    quote: String,
    recordSep: String,
    sep: String,
    timestampFormat: String,
    timestampNTZFormat: String
) extends ResponseToDataFrameConverter {

  def convertResponseToDataFrame(
      restonomerResponseBody: Seq[String]
  )(using sparkSession: SparkSession): DataFrame =
    CSVTextToDataFrameReader(
      columnNameOfCorruptRecord = columnNameOfCorruptRecord,
      dateFormat = dateFormat,
      emptyValue = emptyValue,
      enforceSchema = enforceSchema,
      escape = escape,
      header = header,
      inferSchema = inferSchema,
      ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace,
      ignoreTrailingWhiteSpace = ignoreTrailingWhiteSpace,
      lineSep = lineSep,
      locale = locale,
      multiLine = multiLine,
      nanValue = nanValue,
      nullValue = nullValue,
      originalSchema = originalSchema.map(StructType.fromDDL),
      quote = quote,
      recordSep = recordSep,
      sep = sep,
      timestampFormat = timestampFormat,
      timestampNTZFormat = timestampNTZFormat
    ).read(restonomerResponseBody)

}
