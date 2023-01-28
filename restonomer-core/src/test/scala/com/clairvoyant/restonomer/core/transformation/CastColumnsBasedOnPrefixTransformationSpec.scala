package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.DataFrame

class CastColumnsBasedOnPrefixTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = CastColumnsBasedOnPrefix(
      columnNames = List("col_A","col_B", "col_C"),
      dataTypeToCast = "String"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C"))
      .toDF("col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
