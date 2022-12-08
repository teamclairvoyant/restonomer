package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.spark.utils.transformer.DataFrameTransformerImplicits._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.functions.{col, to_json}

sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddLiteralColumn(
    columnName: String,
    columnValue: String,
    columnDataType: Option[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addColumn(
      columnName = columnName,
      columnValue = columnValue,
      columnDataType = columnDataType
    )

}

case class DeleteColumns(
    columnNames: Set[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.dropColumns(columnNames)

}

case class ConvertColumnToJson(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF
      .withColumn(columnName, to_json(col(columnName)))

}
