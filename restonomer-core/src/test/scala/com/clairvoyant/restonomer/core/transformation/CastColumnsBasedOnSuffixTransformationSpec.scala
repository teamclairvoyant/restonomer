package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.*

class CastColumnsBasedOnSuffixTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "name": "abc",
      |  "india_price": "240",
      |  "US_price": "3",
      |  "percent_difference": "10.23"
      |}
      |""".stripMargin
  )

  "transform()" should "cast columns with the given suffix to the given type" in {
    val restonomerTransformation = CastColumnsBasedOnSuffix(
      suffix = "price",
      dataType = "float"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "name")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "india_price")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "US_price")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "percent_difference")
      .head
      .dataType shouldBe StringType
  }

}
