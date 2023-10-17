package com.clairvoyant.restonomer.body

import com.clairvoyant.data.scalaxy.reader.text.TextToDataFrameReader
import com.clairvoyant.data.scalaxy.reader.text.formats.*
import com.clairvoyant.data.scalaxy.reader.text.instances.*
import org.apache.spark.sql.{DataFrame, SparkSession}
import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait RestonomerResponseBody:
  def read(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame

case class Text(
    textFormat: TextFormat
) extends RestonomerResponseBody:

  override def read(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame =
    textFormat match {
      case csvTextFormat: CSVTextFormat =>
        TextToDataFrameReader
          .read[CSVTextFormat](
            text = restonomerResponseBody,
            textFormat = csvTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case jsonTextFormat: JSONTextFormat =>
        TextToDataFrameReader
          .read[JSONTextFormat](
            text = restonomerResponseBody,
            textFormat = jsonTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case xmlTextFormat: XMLTextFormat =>
        TextToDataFrameReader
          .read[XMLTextFormat](
            text = restonomerResponseBody,
            textFormat = xmlTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )
      
      case htmlTableTextFomat: HTMLTableTextFormat =>
        TextToDataFrameReader
          .read[HTMLTableTextFormat](
            text = restonomerResponseBody,
            textFormat = htmlTableTextFomat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )
    }
