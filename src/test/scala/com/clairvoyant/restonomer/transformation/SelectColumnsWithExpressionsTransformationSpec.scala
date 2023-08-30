package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import org.apache.spark.sql.DataFrame

class SelectColumnsWithExpressionsTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "id": 1,
      |  "name": "Alice",
      |  "age": 25
      |}
      |""".stripMargin
  )

  "transform()" should "select columns as per provided expressions" in {
    val restonomerTransformation = SelectColumnsWithExpressions(
      columnExpressions = List(
        "id",
        "name as full_name",
        "age + 1 as age_next_year"
      )
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "id": 1,
        |  "full_name": "Alice",
        |  "age_next_year": 26
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
