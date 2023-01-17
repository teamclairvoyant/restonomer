package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class AddSuffixToColumnNamesTransformationSpec extends CoreSpec with DataFrameMatchers {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with nonempty suffix and column list" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "_old",
      columnNames = List("col_A", "col_B")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A_old", "col_B_old", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with nonempty suffix and empty column list" should "transform all the columns of the dataframe as expected" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "_old"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A_old", "col_B_old", "col_C_old")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with empty suffix" should "not alter the dataframe" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = ""
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with nonempty suffix but with few valid and invalid column names" should "ignore invalid columns and alter the valid ones from the dataframe" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "_old",
      columnNames = List("col_A", "fake_col")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A_old", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
