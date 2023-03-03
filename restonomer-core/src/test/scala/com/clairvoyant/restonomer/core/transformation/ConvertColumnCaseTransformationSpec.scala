package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ConvertColumnCaseTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |    "col_a": "1",
          |    "COL_B": "2"
          |}""".stripMargin
      )
    )

  "transform() - with valid column name and case type" should "transform the column case" in {
    val restonomerTransformation = ChangeColumnCase(
      caseType = "lower"
    )

    val expectedRestonomerResponseTransformedDF =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_a": "1",
            |  "col_b": "2"
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
