package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.spark.utils.transformer.DataFrameTransformerImplicits._
import org.apache.spark.sql.DataFrame

sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddLiteralColumn(
    columnName: String,
    columnValue: String,
    columnDataType: Option[String] = None
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

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.drop(columnNames)

}

case class ExplodeColumn(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.explode(columnName)

}

case class CastNestedColumn(
    columnName: String,
    ddl: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castNestedColumn(columnName, ddl)

}

case class FlattenSchema() extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.flattenSchema

}

case class CastColumns(
    columnDataTypeMapper: Map[String, String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumns(columnDataTypeMapper)

}

case class ConvertColumnToJson(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.colToJson(columnName)

}

case class ChangeColumnCase(
    caseType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = {
    restonomerResponseDF.changeColCase( caseType)
  }

}
