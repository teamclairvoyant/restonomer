package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.spark.utils.transformer.DataFrameTransformerImplicits.*
import org.apache.spark.sql.DataFrame
import zio.config.derivation.*

@nameWithLabel
sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddColumn(
    columnName: String,
    columnValue: String,
    valueType: String,
    columnDataType: Option[String] = None
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addColumn(
      columnName = columnName,
      columnValue = columnValue,
      valueType = valueType,
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
    restonomerResponseDF.addPrefixToColumnNames(prefix, columnNames)

}

case class RenameColumns(
    renameColumnMapper: Map[String, String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.renameColumns(renameColumnMapper)

}

case class AddSuffixToColumnNames(
    suffix: String,
    columnNames: List[String] = List()
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addSuffixToColumnNames(suffix, columnNames)

}

case class ChangeColumnCase(
    caseType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.changeCaseOfColumnNames(caseType)

}

case class SelectColumns(
    columnNames: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.selectColumns(columnNames)

}

case class FilterRecords(
    filterCondition: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.filterRecords(filterCondition)

}

case class SplitColumn(
    fromColumn: String,
    delimiter: String,
    toColumns: Map[String, Int]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.splitColumn(fromColumn, delimiter, toColumns)

}

case class CastColumnsBasedOnPrefix(
    prefixList: List[String],
    dataTypeToCast: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumnsBasedOnSubstring(
      substringList = prefixList,
      dataTypeToCast = dataTypeToCast,
      matchType = "prefix"
    )

}

case class CastColumnsBasedOnSuffix(
    suffixList: List[String],
    dataTypeToCast: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumnsBasedOnSubstring(
      substringList = suffixList,
      dataTypeToCast = dataTypeToCast,
      matchType = "suffix"
    )

}

case class CastColumnsBasedOnSubstring(
    substringList: List[String],
    dataTypeToCast: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumnsBasedOnSubstring(
      substringList = substringList,
      dataTypeToCast = dataTypeToCast,
      matchType = "contains"
    )

}
