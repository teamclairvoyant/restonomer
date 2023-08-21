package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.DataFrame

class RenameColumnsTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with all existing columns" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "A",
        "col_B" -> "B",
        "col_C" -> "C"
      )
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSONFromText(
      """
        |{
        |  "A": "val_A",
        |  "B": "val_B",
        |  "C": "val_C"
        |}
        |""".stripMargin
    )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with few existing columns" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "A",
        "col_B" -> "B"
      )
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSONFromText(
      """
        |{
        |  "A": "val_A",
        |  "B": "val_B",
        |  "col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with non existing columns" should "not alter the dataframe" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_D" -> "D"
      )
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
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

  "transform() - with different case" should "transform the dataframe as expected" in {
    val restonomerTransformation = RenameColumns(
      renameColumnMapper = Map(
        "col_A" -> "COL_a",
        "col_B" -> "COL_b",
        "col_C" -> "col_c"
      )
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "COL_a": "val_A",
        |  "COL_b": "val_B",
        |  "col_c": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
