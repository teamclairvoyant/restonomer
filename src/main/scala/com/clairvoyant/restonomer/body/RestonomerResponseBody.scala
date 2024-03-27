package com.clairvoyant.restonomer.body

import com.clairvoyant.data.scalaxy.reader.excel.{ExcelFormat, ExcelToDataFrameReader}
import com.clairvoyant.data.scalaxy.reader.text.TextToDataFrameReader
import com.clairvoyant.data.scalaxy.reader.text.formats.*
import com.clairvoyant.data.scalaxy.reader.text.instances.*
import com.clairvoyant.restonomer.HttpResponseBody
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes
import com.clairvoyant.restonomer.common.ResponseBodyCompressionTypes.*
import org.apache.spark.sql.{DataFrame, SparkSession}
import zio.config.derivation.nameWithLabel

import java.io.{BufferedReader, ByteArrayInputStream, InputStreamReader}
import java.util.zip.GZIPInputStream

@nameWithLabel
sealed trait RestonomerResponseBody:
  val compression: Option[String]

  def read[T](restonomerResponseBody: HttpResponseBody[T])(using sparkSession: SparkSession): DataFrame

case class Text(
    textFormat: TextFormat,
    override val compression: Option[String] = None
) extends RestonomerResponseBody:

  def read[T](restonomerResponseBody: HttpResponseBody[T])(using sparkSession: SparkSession): DataFrame =
    val textResponse: Seq[String] =
      restonomerResponseBody match {
        case Seq(compressedTextResponse: Array[Byte]) =>
          ResponseBodyCompressionTypes(compression.get) match {
            case GZIP =>
              val gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedTextResponse))
              val inputStreamReader = new InputStreamReader(gzipStream)
              val bufferedReader = new BufferedReader(inputStreamReader)
              Iterator.continually(bufferedReader.readLine()).takeWhile(_ != null).toSeq
          }
        case unCompressedTextResponse: Seq[String] => unCompressedTextResponse
      }

    textFormat match {
      case csvTextFormat: CSVTextFormat =>
        TextToDataFrameReader[CSVTextFormat]
          .read(
            text = textResponse,
            textFormat = csvTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case jsonTextFormat: JSONTextFormat =>
        TextToDataFrameReader[JSONTextFormat]
          .read(
            text = textResponse,
            textFormat = jsonTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case xmlTextFormat: XMLTextFormat =>
        TextToDataFrameReader[XMLTextFormat]
          .read(
            text = textResponse,
            textFormat = xmlTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case htmlTableTextFormat: HTMLTableTextFormat =>
        TextToDataFrameReader[HTMLTableTextFormat]
          .read(
            text = textResponse,
            textFormat = htmlTableTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )
    }

case class Excel(
    excelFormat: ExcelFormat,
    override val compression: Option[String] = None
) extends RestonomerResponseBody:

  def read[T](restonomerResponseBody: HttpResponseBody[T])(using sparkSession: SparkSession): DataFrame =
    restonomerResponseBody match {
      case Seq(responseBody: Array[Byte]) =>
        ExcelToDataFrameReader.read(
          bytes = responseBody,
          excelFormat = excelFormat,
          originalSchema = None,
          adaptSchemaColumns = identity
        )
    }
