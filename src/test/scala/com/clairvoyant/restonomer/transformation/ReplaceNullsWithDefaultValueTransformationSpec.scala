package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ReplaceNullsWithDefaultValueTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |[
      |  {
      |    "col_A": null,
      |    "col_B": "val_B1",
      |    "col_C": "val_C1"
      |  },
      |  {
      |    "col_A": "val_A2",
      |    "col_B": "val_B2",
      |    "col_C": null
      |  }
      |]
      |""".stripMargin
  )

  "transform()" should "replace all null values as per the value map" in {
    val restonomerTransformation = ReplaceNullsWithDefaultValue(
      Map(
        "col_A" -> "Default_A",
        "col_B" -> "Default_B",
        "col_C" -> "Default_C"
      )
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |[
        |  {
        |    "col_A": "Default_A",
        |    "col_B": "val_B1",
        |    "col_C": "val_C1"
        |  },
        |  {
        |    "col_A": "val_A2",
        |    "col_B": "val_B2",
        |    "col_C": "Default_C"
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
