package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ConvertJSONStringToStructTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "{\"col_B\":\"val_B1\",\"col_C\":\"val_C1\"}"
      |}
      |""".stripMargin
  )

  "transform()" should "convert the specified column to Struct Type" in {
    val restonomerTransformation = ConvertJSONStringToStruct(
      columnName = "col_A"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": {
        |    "col_B": "val_B1",
        |    "col_C": "val_C1"
        |  }
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
