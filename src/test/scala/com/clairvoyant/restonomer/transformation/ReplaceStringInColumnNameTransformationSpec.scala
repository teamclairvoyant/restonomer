package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ReplaceStringInColumnNameTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": 5,
      |  "col_B": 4,
      |  "col_D": {
      |     "col_B": 6
      |   },
      |  "col_F": [
      |    {
      |       "col_B": 4.356343
      |    }
      |  ]
      |}
      |""".stripMargin
  )

  "transform() - with replaceRecursively as false" should "transform the dataframe as expected" in {
    val restonomerTransformation = ReplaceStringInColumnName(
      columnName = "col_B",
      pattern = "_B",
      replacement = "_B_test"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": 5,
        |  "col_B_test": 4,
        |  "col_D": {
        |     "col_B": 6
        |   },
        |  "col_F": [
        |    {
        |       "col_B": 4.356343
        |    }
        |  ]
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with replaceRecursively as true" should "transform the dataframe as expected" in {
    val restonomerTransformation = ReplaceStringInColumnName(
      columnName = "col_B",
      pattern = "_B",
      replacement = "_B_test",
      replaceRecursively = true
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": 5,
        |  "col_B_test": 4,
        |  "col_D": {
        |     "col_B_test": 6
        |   },
        |  "col_F": [
        |    {
        |       "col_B_test": 4.356343
        |    }
        |  ]
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
