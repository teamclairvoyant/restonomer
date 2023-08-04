package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class DeleteColumnsTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with valid column names" should "deleteColumns the columns" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = List("col_B", "col_C")
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A": "val_A"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with invalid column names" should "not throw any exception and return the dataframe" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = List("col_D")
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with empty column names" should "not throw any exception and return the dataframe" in {
    val restonomerTransformation = DeleteColumns(
      columnNames = List()
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
