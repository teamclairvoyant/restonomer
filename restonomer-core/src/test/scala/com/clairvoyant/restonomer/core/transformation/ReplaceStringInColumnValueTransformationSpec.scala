package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.DataFrame

class ReplaceStringInColumnValueTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C",
      |  "col_D": "val_D"
      |}
      |""".stripMargin
  )

  "transform() - with column-Replace-value" should "transform the dataframe as expected" in {
    val restonomerTransformation = ReplaceStringInColumnValue(
      columnName = "col_D",
      pattern = "val_D",
      replacement = "value_D"
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": "val_C",
        |  "col_D": "value_D"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
