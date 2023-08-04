package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.common.CoreSpec
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.expr

class AddColumnTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
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
      columnValueType = "literal",
      columnValue = "val_D",
      columnDataType = Some("string")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
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
      columnValueType = "literal",
      columnValue = "val_D",
      columnDataType = None
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
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
      columnValueType = "literal",
      columnValue = "1",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
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

  "transform() - with expression and without casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValueType = "expression",
      columnValue = "col_c * 2"
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
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
    actualRestonomerResponseTransformedDF.show(10, false)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with expression and casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValueType = "expression",
      columnValue = "col_c * 2",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
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
