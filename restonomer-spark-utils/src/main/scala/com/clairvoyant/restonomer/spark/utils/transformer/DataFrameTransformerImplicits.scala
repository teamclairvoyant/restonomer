package com.clairvoyant.restonomer.spark.utils.transformer

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame}

object DataFrameTransformerImplicits {

  implicit class DataFrameWrapper(df: DataFrame) {

    def addLiteralColumn(
        columnName: String,
        columnValue: String,
        columnDataType: Option[String]
    ): DataFrame =
      columnDataType
        .map(dataType => df.withColumn(columnName, lit(columnValue).cast(dataType)))
        .getOrElse(df.withColumn(columnName, lit(columnValue)))

    def deleteColumns(columnNames: List[String]): DataFrame = df.drop(columnNames: _*)

    def explodeColumn(columnName: String): DataFrame = df.withColumn(columnName, explode(col(columnName)))

    def castNestedColumn(
        columnName: String,
        ddl: String
    ): DataFrame = df.withColumn(columnName, from_json(to_json(col(columnName)), DataType.fromDDL(ddl)))

    def flattenSchema: DataFrame = {
      def flattenSchemaFromStructType(
          schema: StructType,
          prefix: Option[String] = None
      ): Array[Column] =
        schema.fields.flatMap { field =>
          val newColName = prefix.map(p => s"$p.${field.name}").getOrElse(field.name)

          field.dataType match {
            case st: StructType =>
              flattenSchemaFromStructType(st, Some(newColName))
            case _ =>
              Array(col(newColName).as(newColName.replace(".", "_")))
          }
        }

      if (df.schema.exists(_.dataType.isInstanceOf[StructType]))
        df.select(flattenSchemaFromStructType(df.schema): _*)
      else
        df
    }

    def castColumns(columnDataTypeMapper: Map[String, String]): DataFrame =
      df.select(
        df.columns.map { columnName =>
          columnDataTypeMapper
            .get(columnName)
            .map(col(columnName).cast)
            .getOrElse(col(columnName))
        }: _*
      )

    def convertColumnToJson(columnName: String): DataFrame = df.withColumn(columnName, to_json(col(columnName)))

    def replaceStringInColumnValue(columnName: String, pattern: String, replacement: String): DataFrame =
      df.withColumn(columnName, regexp_replace(col(columnName), pattern, replacement))

    def addSuffixToColNames(suffix: String, columnNames: List[String]): DataFrame = {

      if (columnNames.isEmpty)
        df.select(
          df.columns.map(columnName => df(columnName).alias(columnName + "_" + suffix)): _*
        )
      else
        df.select(
          df.columns.map { columnName =>
            df(columnName).alias(
              if (columnNames.contains(columnName))
                columnName + "_" + suffix
              else
                columnName
            )
          }: _*
        )
    }

    def changeColCase(caseType: String): DataFrame = {

      def changeColCaseFunc(colName: String, caseType: String): String =
        caseType match {
          case "upper" =>
            colName.toUpperCase
          case "lower" =>
            colName.toLowerCase
          case _ =>
            throw new Exception("Given caseConversion not supported")
        }

      def parseNestedCol(schema: StructType, caseType: String): StructType = {
        def recurChngCase(schema: StructType): Seq[StructField] =
          schema.fields.map {
            case StructField(name, dtype: StructType, nullable, meta) =>
              StructField(changeColCaseFunc(name, caseType), StructType(recurChngCase(dtype)), nullable, meta)
            case StructField(name, dtype, nullable, meta) =>
              StructField(changeColCaseFunc(name, caseType), dtype, nullable, meta)
          }

        StructType(recurChngCase(schema))
      }
      df.sparkSession.createDataFrame(df.rdd, parseNestedCol(df.schema, caseType))
    }

    def selectColumns(columnNames: List[String]): DataFrame = df.select(columnNames.map(col): _*)

  }

}
