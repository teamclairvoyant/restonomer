package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class ConvertColumnToJsonTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
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

    val expectedRestonomerResponseTransformedDF = readJSON(
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
