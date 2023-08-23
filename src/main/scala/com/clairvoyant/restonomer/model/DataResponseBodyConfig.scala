package com.clairvoyant.restonomer.model

import com.clairvoyant.data.scalaxy.reader.text.*
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait DataResponseBodyConfig:

  def convertResponseToDataFrame(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame

case class CSV(
    columnNameOfCorruptRecord: String = "_corrupt_record",
    dateFormat: String = "yyyy-MM-dd",
    emptyValue: String = "",
    enforceSchema: Boolean = false,
    escape: String = "\\",
    header: Boolean = true,
    inferSchema: Boolean = true,
    ignoreLeadingWhiteSpace: Boolean = false,
    ignoreTrailingWhiteSpace: Boolean = false,
    lineSep: String = "\n",
    locale: String = "en-US",
    multiLine: Boolean = false,
    nanValue: String = "NaN",
    nullValue: String = "null",
    originalSchema: Option[String] = None,
    quote: String = "\"",
    recordSep: String = "\n",
    sep: String = ",",
    timestampFormat: String = "yyyy-MM-dd HH:mm:ss",
    timestampNTZFormat: String = "yyyy-MM-dd'T'HH:mm:ss[.SSS]"
) extends DataResponseBodyConfig:

  override def convertResponseToDataFrame(
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

case class JSON(
    columnNameOfCorruptRecord: String = "_corrupt_record",
    dataColumnName: Option[String] = None,
    dateFormat: String = "yyyy-MM-dd",
    inferSchema: Boolean = true,
    locale: String = "en-US",
    multiLine: Boolean = false,
    originalSchema: Option[String] = None,
    primitivesAsString: Boolean = false,
    timestampFormat: String = "yyyy-MM-dd HH:mm:ss",
    timestampNTZFormat: String = "yyyy-MM-dd'T'HH:mm:ss[.SSS]"
) extends DataResponseBodyConfig:

  override def convertResponseToDataFrame(restonomerResponseBody: Seq[String])(
      using sparkSession: SparkSession
  ): DataFrame =
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
