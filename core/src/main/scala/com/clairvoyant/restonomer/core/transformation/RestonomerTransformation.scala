package com.clairvoyant.restonomer.core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, lit}

sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddColumn(
    columnName: String,
    columnValue: String,
    columnDataType: Option[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = {
    val transformedDF = restonomerResponseDF.withColumn(columnName, lit(columnValue))

    columnDataType
      .map(dataType => transformedDF.withColumn(columnName, col(columnName).cast(dataType)))
      .getOrElse(transformedDF)
  }

}
