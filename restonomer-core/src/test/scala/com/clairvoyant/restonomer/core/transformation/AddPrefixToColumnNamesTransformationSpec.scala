package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class AddPrefixToColumnNamesTransformationSpec extends CoreSpec with DataFrameMatchers {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with nonempty prefix and column list" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test_",
      columnNames = List("col_A", "col_B")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("test_col_A", "test_col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with nonempty prefix and empty column list" should "transform all the columns of the dataframe as expected" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test_"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("test_col_A", "test_col_B", "test_col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with empty prefix" should "not alter the dataframe" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = ""
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with nonempty prefix but with few valid and invalid column names" should "ignore invalid columns and alter the valid ones from the dataframe" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test_",
      columnNames = List("col_A", "fake_col")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("test_col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
