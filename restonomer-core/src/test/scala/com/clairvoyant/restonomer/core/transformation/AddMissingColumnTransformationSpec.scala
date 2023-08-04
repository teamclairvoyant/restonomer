package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.common.CoreSpec

class AddMissingColumnTransformationSpec extends CoreSpec with DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with details of missing columns" should "Add the columns" in {
    val restonomerTransformation = AddMissingColumn(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = "String"
    )

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "val_B",
        |  "col_C": "val_C",
        |  "col_D": "val_D"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - if columns are already present" should "return the original dataframe" in {
    val restonomerTransformation = AddMissingColumn(
      columnName = "col_A",
      columnValue = "val_A",
      columnDataType = "String"
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )

  }

}
