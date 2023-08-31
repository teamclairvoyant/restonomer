package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ConvertArrayOfStructToArrayOfJSONStringTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": [
      |    {
      |      "col_B": "val_B1",
      |      "col_C": "val_C1"
      |    },
      |    {
      |      "col_B": "val_B2",
      |      "col_C": "val_C2"
      |    }
      |  ]
      |}
      |""".stripMargin
  )

  "transform()" should "convert all columns of array of struct type to array of string type" in {
    val restonomerTransformation = ConvertArrayOfStructToArrayOfJSONString()

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": [
        |    "{\"col_B\":\"val_B1\",\"col_C\":\"val_C1\"}",
        |    "{\"col_B\":\"val_B2\",\"col_C\":\"val_C2\"}"
        |  ]
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
