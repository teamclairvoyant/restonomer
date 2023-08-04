package com.clairvoyant.restonomer.spark.utils.transformer

import com.clairvoyant.restonomer.spark.utils.transformer.DataFrameTransformerHelper.*
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.types.*
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
          case "expression" => s"$columnValue"
          case "literal"    => s"'$columnValue'"
          case _            => throw new Exception(s"The provided columnValueType: $columnValueType is not supported.")
        }

      columnDataType
        .map(dataType => df.withColumn(columnName, expr(expression).cast(dataType)))
        .getOrElse(df.withColumn(columnName, expr(expression)))
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

    def castColumns(columnDataTypeMapper: Map[String, String]): DataFrame = {
      val timestampDataTypeRegexPattern = "timestamp(?:\\((.*)\\))?".r
      val dateDataTypeRegexPattern = "date(?:\\((.*)\\))?".r

      df.select(
        df.columns
          .map { columnName =>
            columnDataTypeMapper
              .get(columnName)
              .map {
                case timestampDataTypeRegexPattern(timestampFormat) =>
                  {
                    Option(timestampFormat) match {
                      case Some(timestampFormat) => to_timestamp(col(columnName), timestampFormat)
                      case None                  => to_timestamp(col(columnName))
                    }
                  }.as(columnName)
                case dateDataTypeRegexPattern(dateFormat) =>
                  {
                    Option(dateFormat) match {
                      case Some(dateFormat) => to_date(col(columnName), dateFormat)
                      case None             => to_date(col(columnName))
                    }
                  }.as(columnName)
                case dataType => col(columnName).cast(dataType)
              }
              .getOrElse(col(columnName))
          }*
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
            case "prefix"   => c => substringList.exists(c.startsWith)
            case "suffix"   => c => substringList.exists(c.endsWith)
            case "contains" => c => substringList.exists(c.contains)
          }
        }
        .foldLeft(df) { (df, colName) => df.withColumn(colName, col(colName).cast(dataTypeToCast)) }

    def castFromToDataTypes(
        dataTypeMapper: Map[String, String],
        castRecursively: Boolean
    ): DataFrame =
      dataTypeMapper.foldLeft(df) { (dataFrame, dataTypesPair) =>
        val fromDataType = CatalystSqlParser.parseDataType(dataTypesPair._1)
        val toDataType = CatalystSqlParser.parseDataType(dataTypesPair._2)

        if (castRecursively) {
          def applyCastFunctionRecursively(
              schema: StructType,
              fromDataType: DataType,
              toDataType: DataType
          ): StructType =
            StructType(
              schema.flatMap {
                case sf @ StructField(_, ArrayType(arrayNestedType: StructType, containsNull), _, _) =>
                  StructType(
                    Seq(
                      sf.copy(
                        dataType = ArrayType(
                          applyCastFunctionRecursively(arrayNestedType, fromDataType, toDataType),
                          containsNull
                        )
                      )
                    )
                  )

                case sf @ StructField(_, structType: StructType, _, _) =>
                  StructType(
                    Seq(
                      sf.copy(
                        dataType = applyCastFunctionRecursively(structType, fromDataType, toDataType)
                      )
                    )
                  )

                case sf @ StructField(_, dataType: DataType, _, _) =>
                  StructType(
                    Seq(
                      if (dataType == fromDataType)
                        sf.copy(dataType = toDataType)
                      else
                        sf
                    )
                  )
              }
            )

          val newSchema = applyCastFunctionRecursively(dataFrame.schema, fromDataType, toDataType)
          dataFrame.sparkSession.read.schema(newSchema).json(dataFrame.toJSON)
        } else
          dataFrame.select(
            dataFrame.schema.map { structField =>
              if (structField.dataType == fromDataType)
                col(structField.name).cast(toDataType)
              else
                col(structField.name)
            }.toList*
          )
      }

    def castNestedColumn(
        columnName: String,
        ddl: String
    ): DataFrame = df.withColumn(columnName, from_json(to_json(col(columnName)), DataType.fromDDL(ddl)))

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

    def changeCaseOfColumnNames(sourceCaseType: String, targetCaseType: String): DataFrame = {
      val converter =
        targetCaseType.toLowerCase() match {
          case "camel"  => new CamelCaseConverter()
          case "snake"  => new SnakeCaseConverter()
          case "pascal" => new PascalCaseConverter()
          case "kebab"  => new KebabCaseConverter()
          case "lower"  => new LowerCaseConverter()
          case "upper"  => new UpperCaseConverter()
          case _        => throw new Exception(s"The provided caseType: $targetCaseType is not supported.")
        }

      val renamedColumnNames: Seq[String] = df.columns.map { columnName =>
        converter.convert(columnName, sourceCaseType)
      }

      df.select(df.columns.zip(renamedColumnNames).map { case (original, renamed) => col(original).as(renamed) }: _*)
    }

    def convertColumnToJson(columnName: String): DataFrame = df.withColumn(columnName, to_json(col(columnName)))

    def deleteColumns(columnNames: List[String]): DataFrame = df.drop(columnNames*)

    def explodeColumn(columnName: String): DataFrame = df.withColumn(columnName, explode(col(columnName)))

    def filterRecords(filterCondition: String): DataFrame = df.filter(filterCondition)

    def flattenSchema: DataFrame = {
      def flattenSchemaFromStructType(
          schema: StructType,
          prefix: Option[String] = None
      ): Array[Column] =
        schema.fields.flatMap { field =>
          val newColName = prefix.map(p => s"$p.${field.name}").getOrElse(field.name)

          field.dataType match {
            case st: StructType => flattenSchemaFromStructType(st, Some(newColName))
            case _              => Array(col(newColName).as(newColName.replace(".", "_")))
          }
        }

      if (df.schema.exists(_.dataType.isInstanceOf[StructType]))
        df.select(flattenSchemaFromStructType(df.schema)*)
      else
        df
    }

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

    def replaceEmptyStringsWithNulls: DataFrame = df.na.replace(df.columns, Map("" -> null))

    def replaceStringInColumnValue(columnName: String, pattern: String, replacement: String): DataFrame =
      df.withColumn(columnName, regexp_replace(col(columnName), pattern, replacement))

    def selectColumns(columnNames: List[String]): DataFrame = df.select(columnNames.map(col)*)

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

    def addMissingColumn(columnName: String, columnValue: String, columnDataType: String): DataFrame =
      if (df.columns.contains(columnName))
        df
      else
        df.withColumn(columnName, lit(columnValue).cast(columnDataType))

  }

}
