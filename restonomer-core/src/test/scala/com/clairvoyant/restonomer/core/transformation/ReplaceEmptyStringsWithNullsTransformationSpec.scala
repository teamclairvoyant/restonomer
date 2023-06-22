package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StringType

class ReplaceEmptyStringsWithNullsTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "",
          |  "col_B": "val_B",
          |  "col_C": ""
          |}
          |""".stripMargin
      )
    )

  "transform()" should "replace all empty strings with nulls" in {
    val restonomerTransformation = ReplaceEmptyStringsWithNulls()

    val expectedRestonomerResponseTransformedDF =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": null,
            |  "col_B": "val_B",
            |  "col_C": null
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_A")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_C")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
