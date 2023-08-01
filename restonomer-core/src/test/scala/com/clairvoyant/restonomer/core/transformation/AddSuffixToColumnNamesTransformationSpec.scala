package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.DataFrame

class AddSuffixToColumnNamesTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with suffix and column list" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "old",
      columnNames = List("col_A", "col_B")
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A_old": "val_A",
        |  "col_B_old": "val_B",
        |  "col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with suffix and empty column list" should "transform all the columns of the dataframe as expected" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "old"
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A_old": "val_A",
        |  "col_B_old": "val_B",
        |  "col_C_old": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with suffix but with few valid and invalid column names" should "ignore invalid columns and alter the valid ones from the dataframe" in {
    val restonomerTransformation = AddSuffixToColumnNames(
      suffix = "old",
      columnNames = List("col_A", "fake_col")
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A_old": "val_A",
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
