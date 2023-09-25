package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class ConcatColumnsTransformationSpec extends DataFrameReader with DataFrameMatcher {

  "transform()" should "concat all specified columns" in {
    val restonomerResponseDF = readJSONFromText(
      """
        |{
        |  "street": "Baner Road",
        |  "city": "Pune",
        |  "country": "India"
        |}""".stripMargin
    )

    val restonomerTransformation = ConcatColumns(
      newColumnName = "address",
      columnsToBeConcatenated = List("street", "city", "country"),
      separator = ","
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "street": "Baner Road",
        |  "city": "Pune",
        |  "country": "India",
        |  "address": "Baner Road,Pune,India"
        |}""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

}
