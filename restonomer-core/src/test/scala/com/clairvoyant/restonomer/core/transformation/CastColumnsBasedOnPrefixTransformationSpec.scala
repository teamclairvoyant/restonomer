package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import org.apache.spark.sql.types.*

class CastColumnsBasedOnPrefixTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "name": "abc",
      |  "price_in_india": "240",
      |  "price_in_canada": "3",
      |  "percent_difference": "10.23"
      |}
      |""".stripMargin
  )

  "transform()" should "cast columns with the given prefix to the given type" in {
    val restonomerTransformation = CastColumnsBasedOnPrefix(
      prefix = "price",
      dataType = "float"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "name")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_india")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_canada")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "percent_difference")
      .head
      .dataType shouldBe StringType
  }

}
