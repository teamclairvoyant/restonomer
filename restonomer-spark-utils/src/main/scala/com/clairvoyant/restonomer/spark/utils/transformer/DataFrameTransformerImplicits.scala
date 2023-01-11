package com.clairvoyant.restonomer.spark.utils.transformer

import org.apache.spark.sql.functions.{col, from_json, lit, to_json}
import org.apache.spark.sql.types.{DataType, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame}

object DataFrameTransformerImplicits {

  implicit class DataFrameWrapper(df: DataFrame) {

    def addColumn(
        columnName: String,
        columnValue: String,
        columnDataType: Option[String]
    ): DataFrame =
      columnDataType
        .map(dataType => df.withColumn(columnName, lit(columnValue).cast(dataType)))
        .getOrElse(df.withColumn(columnName, lit(columnValue)))

    def drop(columnNames: Set[String]): DataFrame = df.drop(columnNames.toList: _*)

    def explode(columnName: String): DataFrame =
      df.withColumn(columnName, org.apache.spark.sql.functions.explode(col(columnName)))

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

    def colToJson(columnName: String): DataFrame = df.withColumn(columnName, to_json(col(columnName)))

    def changeColCase(caseType: String): DataFrame = {

      def changeColCaseFunc(colName: String, caseType: String): String =
        caseType match {
          case "upper" =>
            colName.toUpperCase
          case "lower" =>
            colName.toLowerCase
          case _ =>
            colName
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
      println(parseNestedCol(df.schema, caseType))
      df.sparkSession.createDataFrame(df.rdd, parseNestedCol(df.schema, caseType))
    }

  }

}
