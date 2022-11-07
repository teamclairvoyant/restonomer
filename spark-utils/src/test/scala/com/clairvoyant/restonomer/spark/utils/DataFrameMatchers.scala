package com.clairvoyant.restonomer.spark.utils

import cats.data.Validated
import cats.implicits._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row}
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.{MatchResult, Matcher}

import scala.jdk.CollectionConverters.ListHasAsScala

trait DataFrameMatchers {
  self: Matchers =>

  private def collectSorted(dataFrame: DataFrame, columnsToSortBy: List[String]): List[Row] =
    dataFrame.sort(columnsToSortBy.head, columnsToSortBy.tail: _*).collectAsList().asScala.toList

  private def validateSize(actualDFRows: List[Row], expectedDFRows: List[Row]): Validated[String, Unit] =
    Validated.cond(
      test = actualDFRows.size == expectedDFRows.size,
      a = (),
      e = s"Size of actual DF (${actualDFRows.size}) does not match size of expected DF (${expectedDFRows.size})"
    )

  private def validateColumns(
      actualDFColumns: Set[String],
      expectedDFColumns: Set[String]
  ): Validated[String, Unit] =
    Validated.cond(
      test = actualDFColumns == expectedDFColumns,
      a = (),
      e =
        s"""Actual DF has different columns than Expected DF
           |Actual DF columns: ${actualDFColumns.toList.sorted.mkString(",")}
           |Expected DF columns: ${expectedDFColumns.toList.sorted.mkString(",")}
           |Extra columns: ${(actualDFColumns -- expectedDFColumns).toList.sorted.mkString(",")}
           |Missing columns ${(expectedDFColumns -- actualDFColumns).toList.sorted.mkString(",")}
       """.stripMargin
    )

  private def validateSchema(actualDFSchema: StructType, expectedDFSchema: StructType): Validated[String, Unit] = {
    val actualDFSchemaSorted = actualDFSchema.fields.sortBy(_.name)
    val expectedDFSchemaSorted = expectedDFSchema.fields.sortBy(_.name)

    val nonMatchingFieldsPairs = actualDFSchemaSorted.zip(expectedDFSchemaSorted).filter {
      case (actualDFStructField, expectedDFStructField) =>
        actualDFStructField.dataType != expectedDFStructField.dataType
    }

    Validated.cond(
      test = nonMatchingFieldsPairs.isEmpty,
      a = (),
      e =
        s"""Actual DF has different column types than Expected DF
           |Actual DF columns: ${StructType(nonMatchingFieldsPairs.map(_._1).sortBy(_.name)).treeString}
           |Expected DF columns: ${StructType(nonMatchingFieldsPairs.map(_._2).sortBy(_.name)).treeString}
           |Non matching columns: ${nonMatchingFieldsPairs.map { case (actualDFStructField, expectedDFStructField) =>
          (actualDFStructField.toDDL, expectedDFStructField.toDDL)
        }.toList}
         """.stripMargin
    )
  }

  def validateRows(
      actualDFRows: List[Row],
      expectedDFRows: List[Row],
      columns: Seq[String]
  ): Validated[String, Unit] = {
    val actualDFRowsValues = actualDFRows.map(_.getValuesMap(columns))
    val expectedDFRowsValues = expectedDFRows.map(_.getValuesMap(columns))

    def validateRow(
        actualDFRow: Map[String, Any],
        expectedDFRow: Map[String, Any],
        rowNumber: Int
    ): Validated[String, Unit] = {
      expectedDFRow.toList.traverse_ { case (fieldName, expectedDFRowValue) =>
        val actualDFRowValueOption = actualDFRow.get(fieldName)
        val actualDFRowValueClassOption = actualDFRowValueOption.map(_.getClass)

        val expectedDFRowValueOption = Option(expectedDFRowValue)
        val expectedDFRowValueClassOption = expectedDFRowValueOption.map(_.getClass)

        Validated.cond(
          test = actualDFRowValueOption == expectedDFRowValue,
          a = (),
          e =
            s"Row: $rowNumber, field: $fieldName: ${actualDFRowValueOption.orNull} (${actualDFRowValueClassOption.orNull}) does not match expected ${expectedDFRowValueOption.orNull} (${expectedDFRowValueClassOption.orNull})"
        )
      }
    }

    actualDFRowsValues.zip(expectedDFRowsValues).zipWithIndex.traverse_ {
      case ((actualDFRow, expectedDFRow), rowNumber) =>
        validateRow(actualDFRow, expectedDFRow, rowNumber)
    }
  }

  def matchExpectedDataFrame(
      expectedDF: DataFrame,
      columnsToSort: List[String] = List.empty
  ): Matcher[DataFrame] =
    (actualDF: DataFrame) => {
      val actualDFColumns = actualDF.columns.toSet
      val expectedDFColumns = expectedDF.columns.toSet

      val columnsToSortBy =
        if (columnsToSort.isEmpty)
          actualDFColumns.intersect(expectedDFColumns).toList
        else
          columnsToSort

      if (columnsToSortBy.isEmpty) {
        throw new RuntimeException(
          s"""We can not intersect any column from expected to actual.
             |Actual DF columns: ${actualDFColumns.toList.sorted.mkString(", ")}
             |Expected DF columns: ${expectedDFColumns.toList.sorted.mkString(", ")}
          """
        )
      }

      val actualDFRows = collectSorted(actualDF, columnsToSortBy)
      val expectedDFRows = collectSorted(expectedDF, columnsToSortBy)

      val columnsValidation = validateColumns(actualDFColumns, expectedDFColumns)
      val sizeValidation = validateSize(actualDFRows, expectedDFRows)
      val schemaValidation = validateSchema(actualDF.schema, expectedDF.schema)
      lazy val rowsValidation = validateRows(actualDFRows, expectedDFRows, actualDFColumns.toSeq.sorted)

      val allValidations = columnsValidation
        .combine(sizeValidation)
        .combine(schemaValidation)
        .andThen((_: Unit) => rowsValidation)

      val validationMessages = allValidations.fold(_.toList, _ => List.empty)

      MatchResult(
        matches = allValidations.isValid,
        rawFailureMessage =
          s"""Content of data frame does not match expected data.
             |${validationMessages.mkString("\n")}
             |""".stripMargin,
        rawNegatedFailureMessage = "Content of actual data frame matches expected data frame"
      )
    }

}
