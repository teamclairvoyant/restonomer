package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class AddColumnTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with column-data-type" should "transform the dataframe" in {
    val restonomerTransformation = AddColumnTransformation(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = Some("string")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C", "val_D"))
      .toDF("col_A", "col_B", "col_C", "col_D")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF,
      columnsToSort = expectedRestonomerResponseTransformedDF.columns.toList
    )
  }

}
