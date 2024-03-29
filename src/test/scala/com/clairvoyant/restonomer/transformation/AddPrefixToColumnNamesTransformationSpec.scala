package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class AddPrefixToColumnNamesTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with prefix and column list" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test",
      columnNames = List("col_A", "col_B")
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "test_col_A": "val_A",
        |  "test_col_B": "val_B",
        |  "col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with prefix and empty column list" should "transform all the columns of the dataframe as expected" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "test_col_A": "val_A",
        |  "test_col_B": "val_B",
        |  "test_col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with prefix but with few valid and invalid column names" should "ignore invalid columns and alter the valid ones from the dataframe" in {
    val restonomerTransformation = AddPrefixToColumnNames(
      prefix = "test",
      columnNames = List("col_A", "fake_col")
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "test_col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
