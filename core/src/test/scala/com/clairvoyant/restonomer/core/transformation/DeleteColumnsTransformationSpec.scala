package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class DeleteColumnsTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with valid column names" should "drop the columns" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = Set("col_B", "col_C")
    )

    val expectedRestonomerResponseTransformedDF = Seq("val_A")
      .toDF("col_A")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with invalid column names" should "not throw any exception and return the dataframe" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = Set("col_D")
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with empty column names" should "not throw any exception and return the dataframe" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = Set()
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
