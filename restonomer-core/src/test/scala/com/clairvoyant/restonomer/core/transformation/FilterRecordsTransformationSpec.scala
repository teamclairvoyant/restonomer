package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.{AnalysisException, DataFrame}

class FilterRecordsTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |[
          |  {
          |    "col_A": "10",
          |    "col_B": "val_B1",
          |    "col_C": "val_C1"
          |  },
          |  {
          |    "col_A": "20",
          |    "col_B": "val_B2",
          |    "col_C": "val_C2"
          |  }
          |]
          |""".stripMargin
      )
    )

  "transform() - with valid condition" should "filter the dataframe as expected" in {
    val restonomerTransformation = FilterRecords(
      filterCondition = "col_A > 10"
    )

    val expectedRestonomerResponseTransformedDF =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |[
            |  {
            |    "col_A": "20",
            |    "col_B": "val_B2",
            |    "col_C": "val_C2"
            |  }
            |]
            |""".stripMargin
        )
      )

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
