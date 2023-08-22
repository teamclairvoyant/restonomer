package com.clairvoyant.restonomer.core.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.expr
import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class AddColumnTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": 10.2
      |}
      |""".stripMargin
  )

  "transform() - with column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = Some("string")
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": 10.2,
        |  "col_D": "val_D"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - without column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = None
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": 10.2,
        |  "col_D": "val_D"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with int column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "1",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": 10.2,
        |  "col_D": 1
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
