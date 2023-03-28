package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ReplaceStringInColumnValueTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": "val_B",
          |  "col_C": "val_C",
          |  "col_D": "val_D"
          |}
          |""".stripMargin
      )
    )

  "transform() - with column-Replace-value" should "transform the dataframe as expected" in {
    val restonomerTransformation = ReplaceStringInColumnValue(
      columnName = "col_D",
      pattern = "val_D",
      replacement = "value_D"
    )

    val expectedRestonomerResponseTransformedDF =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": "val_C",
            |  "col_D": "value_D"
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
