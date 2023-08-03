package com.clairvoyant.restonomer.core.model

import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait DataResponseBodyConfig

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
) extends DataResponseBodyConfig

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
) extends DataResponseBodyConfig
