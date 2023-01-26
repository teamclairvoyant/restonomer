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
    restonomerResponseDF.addLiteralColumn(
      columnName = columnName,
      columnValue = columnValue,
      columnDataType = columnDataType
    )

}

case class DeleteColumns(
    columnNames: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.deleteColumns(columnNames)

}

case class ExplodeColumn(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.explodeColumn(columnName)

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

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.convertColumnToJson(columnName)

}

case class ReplaceStringInColumnValue(
    columnName: String,
    pattern: String,
    replacement: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.replaceStringInColumnValue(columnName, pattern, replacement)

}


case class AddPrefixToColumnNames(
    prefix: String,
    columnNames: List[String] = List[String]()
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addPrefixToColNames(prefix, columnNames)

case class RenameColumns(
    renameColumnMapper: Map[String, String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.renameCols(renameColumnMapper)

}

case class ChangeColumnCase(
    caseType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.changeColCase(caseType)

}

case class SelectColumns(
    columnNames: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.selectColumns(columnNames)

}
