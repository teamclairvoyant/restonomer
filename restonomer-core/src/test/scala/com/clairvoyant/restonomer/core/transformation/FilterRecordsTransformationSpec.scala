package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.{AnalysisException, DataFrame}

class FilterRecordsTransformationSpec extends CoreSpec with DataFrameMatchers {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("10", "val_B1", "val_C1"), ("20", "val_B2", "val_C2")).toDF(
    "col_A",
    "col_B",
    "col_C"
  )

  "transform() - with valid condition" should "filter the dataframe as expected" in {
    val restonomerTransformation = FilterRecords(
      filterCondition = "col_A > 10"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("20", "val_B2", "val_C2"))
      .toDF("col_A", "col_B", "col_C")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with invalid condition" should "throw AnalysisException" in {
    val restonomerTransformation = FilterRecords(
      filterCondition = "col_D > 10"
    )

    assertThrows[AnalysisException] {
      restonomerTransformation.transform(restonomerResponseDF)
    }
  }

}
