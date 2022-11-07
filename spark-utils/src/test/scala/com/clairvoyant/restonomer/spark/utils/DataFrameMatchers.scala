package com.clairvoyant.restonomer.spark.utils

import cats.data.Validated
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row}
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.should.Matchers

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

      val sizeValidation = validateSize(actualDFRows, expectedDFRows)
      val columnsValidation = validateColumns(actualDFColumns, expectedDFColumns)
      val schemaValidation = validateSchema(actualDF.schema, expectedDF.schema)
    }

}
