package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ConvertColumnToJsonTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |    "col_A": "1",
      |    "col_B": [
      |        {
      |            "Zipcode": 704,
      |            "ZipCodeType": "STANDARD"
      |        }
      |    ]
      |}""".stripMargin
  )

  "transform() - with valid column name" should "transform the column to json" in {
    val restonomerTransformation = ConvertColumnToJson(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |    "col_A": "1",
        |    "col_B": "[{\"ZipCodeType\":\"STANDARD\",\"Zipcode\":704}]"
        |}""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
