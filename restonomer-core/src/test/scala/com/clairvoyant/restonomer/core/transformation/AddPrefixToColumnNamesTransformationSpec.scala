package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class AddPrefixToColumnNamesTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
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

    val expectedRestonomerResponseTransformedDF = readJSON(
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

    val expectedRestonomerResponseTransformedDF = readJSON(
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

    val expectedRestonomerResponseTransformedDF = readJSON(
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
