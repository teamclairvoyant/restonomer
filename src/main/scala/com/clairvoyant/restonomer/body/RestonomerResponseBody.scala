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
    format: TextFormat
) extends RestonomerResponseBody:

  override def read(restonomerResponseBody: Seq[String])(using sparkSession: SparkSession): DataFrame =
    format match {
      case csvTextFormat: CSVTextFormat =>
        TextToDataFrameReader
          .read[CSVTextFormat](
            text = restonomerResponseBody,
            textFormat = csvTextFormat
          )

      case jsonTextFormat: JSONTextFormat =>
        TextToDataFrameReader
          .read[JSONTextFormat](
            text = restonomerResponseBody,
            textFormat = jsonTextFormat
          )

      case xmlTextFormat: XMLTextFormat =>
        TextToDataFrameReader
          .read[XMLTextFormat](
            text = restonomerResponseBody,
            textFormat = xmlTextFormat
          )
    }
