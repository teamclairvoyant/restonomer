package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class RenameColumnsTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with all existing columns" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "A",
        "col_B" -> "B",
        "col_C" -> "C"
      )
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("A", "B", "C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with few existing columns" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "A",
        "col_B" -> "B"
      )
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("A", "B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with non existing columns" should "not alter the dataframe" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_D" -> "D"
      )
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with different case" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "COL_a",
        "col_B" -> "COL_b",
        "col_C" -> "col_c"
      )
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("COL_a", "COL_b", "col_c")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
