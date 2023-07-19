package com.clairvoyant.restonomer.spark.utils.transformer

import com.clairvoyant.restonomer.spark.utils.caseconverter.*
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.types.{ArrayType, DataType, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame}

object DataFrameTransformerImplicits {

  extension (df: DataFrame) {

    def addColumn(
        columnName: String,
        columnValueType: String,
        columnValue: String,
        columnDataType: Option[String]
    ): DataFrame = {
      val expression =
        columnValueType match {
          case "expression" =>
            s"$columnValue"
          case "literal" =>
            s"'$columnValue'"
          case _ =>
            throw new Exception(s"The provided columnValueType: $columnValueType is not supported.")
        }

      columnDataType
        .map(dataType => df.withColumn(columnName, expr(expression).cast(dataType)))
        .getOrElse(df.withColumn(columnName, expr(expression)))
    }

    def deleteColumns(columnNames: List[String]): DataFrame = df.drop(columnNames*)

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
        df.select(flattenSchemaFromStructType(df.schema)*)
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
        }*
      )

    def convertColumnToJson(columnName: String): DataFrame = df.withColumn(columnName, to_json(col(columnName)))

    def replaceStringInColumnValue(columnName: String, pattern: String, replacement: String): DataFrame =
      df.withColumn(columnName, regexp_replace(col(columnName), pattern, replacement))

    def renameColumns(renameColumnMapper: Map[String, String]): DataFrame =
      df.select(
        df.columns
          .map(columnName =>
            renameColumnMapper
              .get(columnName)
              .map(col(columnName).name)
              .getOrElse(col(columnName))
          )*
      )

    private def applyChangeNameFunctionRecursively(
        schema: StructType,
        changeNameFunction: String => String
    ): StructType =
      StructType(
        schema.flatMap {
          case sf @ StructField(
                name,
                ArrayType(arrayNestedType: StructType, containsNull),
                nullable,
                metadata
              ) =>
            StructType(
              Seq(
                sf.copy(
                  changeNameFunction(name),
                  dataType = ArrayType(
                    applyChangeNameFunctionRecursively(arrayNestedType, changeNameFunction),
                    containsNull
                  ),
                  nullable,
                  metadata
                )
              )
            )
          case sf @ StructField(name, structType: StructType, nullable, metadata) =>
            StructType(
              Seq(
                sf.copy(
                  changeNameFunction(name),
                  dataType = applyChangeNameFunctionRecursively(structType, changeNameFunction),
                  nullable,
                  metadata
                )
              )
            )

          case sf @ StructField(name, _, _, _) =>
            StructType(
              Seq(
                sf.copy(name = changeNameFunction(name))
              )
            )
        }
      )

//    def changeCaseOfColumnNames(caseType: String): DataFrame =
//      df.sparkSession.createDataFrame(
//        rowRDD = df.rdd,
//        schema = applyChangeNameFunctionRecursively(
//          schema = df.schema,
//          changeNameFunction =
//            (columnName: String) =>
//              caseType.toLowerCase() match {
//                case "upper" =>
//                  columnName.toUpperCase
//                case "lower" =>
//                  columnName.toLowerCase
//                case _ =>
//                  throw new Exception(s"The provided caseType: $caseType is not supported.")
//              }
//        )
//      )

    def changeCaseOfColumnNames(targetCaseType: String, sourceCaseType: String = "lower"): DataFrame = {

      val converter =
        targetCaseType.toLowerCase() match {
          case "camel" =>
            new CamelCaseConverter()
          case "snake" =>
            new SnakeCaseConverter()
          case "pascal" =>
            new PascalCaseConverter()
          case "kebab" =>
            new KebabCaseConverter()
          case "lower" =>
            new LowerCaseConverter()
          case "upper" =>
            new UpperCaseConverter()
          case _ =>
            throw new Exception(s"The provided caseType: $targetCaseType is not supported.")
        }

      val renamedColumnNames: Seq[String] = df.columns.map { columnName =>
        converter.convert(columnName, sourceCaseType)
      }

      df.select(df.columns.zip(renamedColumnNames).map { case (original, renamed) =>
        col(original).as(renamed)
      }: _*)
    }

    def addPrefixToColumnNames(prefix: String, columnNames: List[String]): DataFrame =
      if (columnNames.isEmpty)
        df.renameColumns(
          df.columns
            .map(columnName => columnName -> s"${prefix}_$columnName")
            .toMap
        )
      else
        df.renameColumns(
          df.columns.map { columnName =>
            if (columnNames.contains(columnName))
              columnName -> s"${prefix}_$columnName"
            else
              columnName -> columnName
          }.toMap
        )

    def addSuffixToColumnNames(suffix: String, columnNames: List[String]): DataFrame =
      if (columnNames.isEmpty)
        df.renameColumns(
          df.columns
            .map(columnName => columnName -> s"${columnName}_$suffix")
            .toMap
        )
      else
        df.renameColumns(
          df.columns.map { columnName =>
            if (columnNames.contains(columnName))
              columnName -> s"${columnName}_$suffix"
            else
              columnName -> columnName
          }.toMap
        )

    def selectColumns(columnNames: List[String]): DataFrame = df.select(columnNames.map(col)*)

    def filterRecords(filterCondition: String): DataFrame = df.filter(filterCondition)

    def splitColumn(
        fromColumn: String,
        delimiter: String,
        toColumns: Map[String, Int]
    ): DataFrame =
      toColumns.foldLeft(df) { (df, columnNamePositionPair) =>
        df.withColumn(
          columnNamePositionPair._1,
          split(col(fromColumn), delimiter).getItem(columnNamePositionPair._2)
        )
      }

    def castColumnsBasedOnSubstring(
        substringList: List[String],
        dataTypeToCast: String,
        matchType: String
    ): DataFrame =
      df.columns
        .filter {
          matchType match {
            case "prefix" =>
              c => substringList.exists(c.startsWith)
            case "suffix" =>
              c => substringList.exists(c.endsWith)
            case "contains" =>
              c => substringList.exists(c.contains)
          }
        }
        .foldLeft(df) { (df, colName) => df.withColumn(colName, col(colName).cast(dataTypeToCast)) }

  }

}
