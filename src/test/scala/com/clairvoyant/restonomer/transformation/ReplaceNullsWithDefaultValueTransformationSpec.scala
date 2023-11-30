package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ReplaceNullsWithDefaultValueTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |[
      |  {
      |    "col_A": null,
      |    "col_B": 5,
      |    "col_C": null
      |  },
      |  {
      |    "col_A": "val_A2",
      |    "col_B": null,
      |    "col_C": 3.4
      |  }
      |]
      |""".stripMargin
  )

  "transform()" should "replace all null values as per the value map" in {
    val restonomerTransformation = ReplaceNullsWithDefaultValue(
      Map(
        "col_A" -> "Default_A",
        "col_B" -> 0,
        "col_C" -> 0.1
      )
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |[
        |  {
        |    "col_A": "Default_A",
        |    "col_B": 5,
        |    "col_C": 0.1
        |  },
        |  {
        |    "col_A": "val_A2",
        |    "col_B": 0,
        |    "col_C": 3.4
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
