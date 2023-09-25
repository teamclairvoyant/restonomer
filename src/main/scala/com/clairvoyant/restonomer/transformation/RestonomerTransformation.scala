package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.transformer.DataFrameTransformerImplicits.*
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.*
import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}

case class AddColumn(
    columnName: String,
    columnValue: String,
    columnDataType: Option[String] = None,
    replaceExisting: Boolean = false
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addColumn(
      columnName = columnName,
      columnValue = columnValue,
      columnDataType = columnDataType,
      replaceExisting = replaceExisting
    )

}

case class AddColumnWithExpression(
    columnName: String,
    columnExpression: String,
    columnDataType: Option[String] = None,
    replaceExisting: Boolean = false
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addColumnWithExpression(
      columnName = columnName,
      columnExpression = columnExpression,
      columnDataType = columnDataType,
      replaceExisting = replaceExisting
    )

}

case class AddPrefixToColumnNames(
    prefix: String,
    columnNames: List[String] = List[String]()
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addPrefixToColumnNames(prefix, columnNames)

}

case class AddSuffixToColumnNames(
    suffix: String,
    columnNames: List[String] = List()
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.addSuffixToColumnNames(suffix, columnNames)

}

case class CastColumns(
    columnDataTypeMapper: Map[String, String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumns(columnDataTypeMapper)

}

case class CastColumnsBasedOnPrefix(
    prefix: String,
    dataType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumnsBasedOnPrefix(
      prefix = prefix,
      dataType = dataType
    )

}

case class CastColumnsBasedOnSuffix(
    suffix: String,
    dataType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castColumnsBasedOnSuffix(
      suffix = suffix,
      dataType = dataType
    )

}

case class CastFromToDataTypes(
    dataTypeMapper: Map[String, String],
    castRecursively: Boolean = false
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castFromToDataTypes(dataTypeMapper, castRecursively)

}

case class CastNestedColumn(
    columnName: String,
    ddl: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.castNestedColumn(columnName, ddl)

}

case class ChangeColumnCase(
    sourceCaseType: String = "lower",
    targetCaseType: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.changeCaseOfColumnNames(
      sourceCaseType = sourceCaseType,
      targetCaseType = targetCaseType
    )

}

case class ConcatColumns(
    newColumnName: String,
    columnsToBeConcatenated: List[String],
    separator: String = ""
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame) =
    restonomerResponseDF.withColumn(newColumnName, concat_ws(separator, columnsToBeConcatenated.map(col)*))

}

case class ConvertArrayOfStructToArrayOfJSONString() extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.convertArrayOfStructToArrayOfJSONString

}

case class ConvertColumnToJson(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.withColumn(columnName, to_json(col(columnName)))

}

case class ConvertJSONStringToStruct(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.convertJSONStringToStruct(columnName)

}

case class DeleteColumns(
    columnNames: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.drop(columnNames*)

}

case class ExplodeColumn(
    columnName: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.withColumn(columnName, explode(col(columnName)))

}

case class FilterRecords(
    filterCondition: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.filter(filterCondition)

}

case class FlattenSchema() extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.flattenSchema

}

case class RenameColumns(
    renameColumnMapper: Map[String, String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.renameColumns(renameColumnMapper)

}

case class ReplaceEmptyStringsWithNulls() extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame = restonomerResponseDF.replaceEmptyStringsWithNulls

}

case class ReplaceStringInColumnValue(
    columnName: String,
    pattern: String,
    replacement: String
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.withColumn(columnName, regexp_replace(col(columnName), pattern, replacement))

}

case class SelectColumns(
    columnNames: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.select(columnNames.map(col)*)

}

case class SelectColumnsWithExpressions(
    columnExpressions: List[String]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.selectExpr(columnExpressions*)

}

case class SplitColumn(
    fromColumn: String,
    delimiter: String,
    toColumns: Map[String, Int]
) extends RestonomerTransformation {

  override def transform(restonomerResponseDF: DataFrame): DataFrame =
    restonomerResponseDF.splitColumn(fromColumn, delimiter, toColumns)

}
