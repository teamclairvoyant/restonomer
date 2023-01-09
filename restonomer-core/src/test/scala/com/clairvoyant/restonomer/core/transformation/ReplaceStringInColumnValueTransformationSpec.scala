package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class ReplaceStringInColumnValueTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_E")).toDF("col_A", "col_B", "col_C")

  "transform() - with column-Replace-value" should "transform the dataframe as expected" in {
    val restonomerTransformation = ReplaceStringInColumnValue(
      columnName = "col_D",
      columnValue = "val_D",
      columnReplaceValue = "value_D"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C", "val_D"))
      .toDF("col_A", "col_B", "col_C", "value_D")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
