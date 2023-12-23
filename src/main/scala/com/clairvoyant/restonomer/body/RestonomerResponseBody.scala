package com.clairvoyant.restonomer.body

import com.clairvoyant.data.scalaxy.reader.text.TextToDataFrameReader
import com.clairvoyant.data.scalaxy.reader.text.formats.*
import com.clairvoyant.data.scalaxy.reader.text.instances.*
import org.apache.spark.sql.{DataFrame, SparkSession}
import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait RestonomerResponseBody:
  val compression: Option[String]

  def read(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame

case class Text(
    textFormat: TextFormat,
    override val compression: Option[String] = None
) extends RestonomerResponseBody:

  override def read(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame =
    textFormat match {
      case csvTextFormat: CSVTextFormat =>
        TextToDataFrameReader[CSVTextFormat]
          .read(
            text = restonomerResponseBody,
            textFormat = csvTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case jsonTextFormat: JSONTextFormat =>
        TextToDataFrameReader[JSONTextFormat]
          .read(
            text = restonomerResponseBody,
            textFormat = jsonTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case xmlTextFormat: XMLTextFormat =>
        TextToDataFrameReader[XMLTextFormat]
          .read(
            text = restonomerResponseBody,
            textFormat = xmlTextFormat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )

      case htmlTableTextFomat: HTMLTableTextFormat =>
        TextToDataFrameReader[HTMLTableTextFormat]
          .read(
            text = restonomerResponseBody,
            textFormat = htmlTableTextFomat,
            originalSchema = None,
            adaptSchemaColumns = identity
          )
    }
