package com.clairvoyant.restonomer.body

import com.clairvoyant.data.scalaxy.reader.excel.{ExcelFormat, ExcelToDataFrameReader}
import com.clairvoyant.data.scalaxy.reader.text.TextToDataFrameReader
import com.clairvoyant.data.scalaxy.reader.text.formats.*
import com.clairvoyant.data.scalaxy.reader.text.instances.*
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes.*
import org.apache.spark.sql.{DataFrame, SparkSession}
import zio.config.derivation.nameWithLabel

import java.io.{BufferedReader, ByteArrayInputStream, InputStreamReader}
import java.util.zip.GZIPInputStream

@nameWithLabel
sealed trait RestonomerResponseBody:
  val compression: Option[String]

  def read[T](restonomerResponseBody: Seq[T])(using sparkSession: SparkSession): DataFrame

case class Text(
    textFormat: TextFormat,
    override val compression: Option[String] = None
) extends RestonomerResponseBody:

  def read[T](restonomerResponseBody: Seq[T])(using sparkSession: SparkSession): DataFrame =
    val finalRestonomerResponseBody =
      restonomerResponseBody match {
        case uncompressedTextResponse: Seq[String] => uncompressedTextResponse
        case compressedTextResponse: Seq[Array[Byte]] =>
          ResponseBodyCompressionTypes(compression.get) match {
            case GZIP =>
              val gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedTextResponse.head))
              val inputStreamReader = new InputStreamReader(gzipStream)
              val bufferedReader = new BufferedReader(inputStreamReader)
              Iterator.continually(bufferedReader.readLine()).takeWhile(_ != null).toSeq
          }
      }

    textFormat match {
      case csvTextFormat: CSVTextFormat =>
        TextToDataFrameReader[CSVTextFormat]
          .read(
            text = finalRestonomerResponseBody,
            textFormat = csvTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case jsonTextFormat: JSONTextFormat =>
        TextToDataFrameReader[JSONTextFormat]
          .read(
            text = finalRestonomerResponseBody,
            textFormat = jsonTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case xmlTextFormat: XMLTextFormat =>
        TextToDataFrameReader[XMLTextFormat]
          .read(
            text = finalRestonomerResponseBody,
            textFormat = xmlTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case htmlTableTextFormat: HTMLTableTextFormat =>
        TextToDataFrameReader[HTMLTableTextFormat]
          .read(
            text = finalRestonomerResponseBody,
            textFormat = htmlTableTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )
    }

case class Excel(
    excelFormat: ExcelFormat,
    override val compression: Option[String] = None
) extends RestonomerResponseBody:

  def read[T](restonomerResponseBody: Seq[T])(using sparkSession: SparkSession): DataFrame =
    restonomerResponseBody match {
      case Seq(responseBody: Array[Byte]) =>
        ExcelToDataFrameReader.read(
          bytes = responseBody,
          excelFormat = excelFormat,
          originalSchema = None,
          adaptSchemaColumns = identity
        )
    }
