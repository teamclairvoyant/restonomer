package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class CoalesceColumnsTransformationSpec extends DataFrameReader with DataFrameMatcher {

  "transform()" should "perform the coalesce operation on listed columns and put the desired value" in {
    val restonomerResponseDF = readJSONFromText(
      """
        |{
        |  "col_A": null,
        |  "col_B": "Pune",
        |  "col_C": null
        |}""".stripMargin
    )

    val restonomerTransformation = CoalesceColumns(
      newColumnName = "col_D",
      columnsToCoalesce = List("col_A", "col_B", "col_C")
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_A": null,
        |  "col_B": "Pune",
        |  "col_C": null,
        |  "col_D": "Pune"
        |}""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with all null" should "perform the coalesce operation on listed columns and put the desired value" in {
    val restonomerResponseDF = readJSONFromText(
      """
        |{
        |  "col_A": null,
        |  "col_B": null,
        |  "col_C": null
        |}""".stripMargin
    )

    val restonomerTransformation = CoalesceColumns(
      newColumnName = "col_D",
      columnsToCoalesce = List("col_A", "col_B", "col_C")
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_A": null,
        |  "col_B": null,
        |  "col_C": null,
        |  "col_D": null
        |}""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

}
