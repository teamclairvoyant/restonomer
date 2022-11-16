package com.clairvoyant.restonomer.core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit

sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddColumn(
    columnName: String,
    columnValue: String,
    columnDataType: Option[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    columnDataType
      .map(dataType => restonomerResponseDF.withColumn(columnName, lit(columnValue).cast(dataType)))
      .getOrElse(restonomerResponseDF.withColumn(columnName, lit(columnValue)))

}
