package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ExplodeColumnTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": ["val_1", "val_2", "val_3"]
      |}
      |""".stripMargin
  )

  "transform() - with valid column name" should "explodeColumn the column into multiple rows" in {
    val restonomerTransformation = ExplodeColumn(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |[
        |  {
        |    "col_A": "val_A",
        |    "col_B": "val_1"
        |  },
        |  {
        |    "col_A": "val_A",
        |    "col_B": "val_2"
        |  },
        |  {
        |    "col_A": "val_A",
        |    "col_B": "val_3"
        |  }
        |]
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
