package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class AddColumnWithExpressionTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": 10.2
      |}
      |""".stripMargin
  )

  "transform() - with expression and without casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumnWithExpression(
      columnName = "col_D",
      columnExpression = "col_C * 2"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": 10.2,
        |  "col_D": 20.4
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with expression and casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumnWithExpression(
      columnName = "col_D",
      columnExpression = "col_C * 2",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": 10.2,
        |  "col_D": 20
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
