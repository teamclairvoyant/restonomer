package com.clairvoyant.restonomer.spark.utils

object StringHelper {

  private val normalizeUmlaut: Map[String, String] = Map(
    "ü" -> "ue",
    "ä" -> "ae",
    "ö" -> "oe",
    "Ü" -> "UE",
    "Ä" -> "AE",
    "Ö" -> "OE",
    "-" -> "_",
    "ß" -> "ss",
    " " -> "_",
    "@" -> "_"
  )

  def hasUmlaut(str: String): Boolean = normalizeUmlaut.keys.exists(key => str.contains(key))

  def sanitiseColumnName(columnName: String): String =
    normalizeUmlaut.foldLeft(columnName) { case (column, (key, value)) =>
      column.replaceAll(key, value)
    }

}
