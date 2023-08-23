package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class SplitColumnTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      | "address": "Apt-123,XYZ Building,Pune,Maharashtra"
      |}
      |""".stripMargin
  )

  "transform()" should "split the column and create new columns accordingly" in {
    val restonomerTransformation = SplitColumn(
      fromColumn = "address",
      delimiter = ",",
      toColumns = Map(
        "apt_number" -> 0,
        "society_name" -> 1,
        "city" -> 2,
        "state" -> 3
      )
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        | "address": "Apt-123,XYZ Building,Pune,Maharashtra",
        | "apt_number": "Apt-123",
        | "society_name": "XYZ Building",
        | "city": "Pune",
        | "state": "Maharashtra"
        |}
        |""".stripMargin
    )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedRestonomerResponseTransformedDF)
  }

}
