package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.*

class CastColumnsBasedOnSuffixTransformationSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "name": "abc",
          |  "india_price": "240",
          |  "US_price": "3",
          |  "percent_difference": "10.23"
          |}
          |""".stripMargin
      )
    )

  "transform()" should "cast columns with the given suffix to the given type" in {
    val restonomerTransformation = CastColumnsBasedOnSuffix(
      suffixList = List("price", "percent"),
      dataTypeToCast = "float"
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

  "transform()" should "skip the suffix having no matches" in {
    val restonomerTransformation = CastColumnsBasedOnPrefix(
      prefixList = List("amount", "value"),
      dataTypeToCast = "double"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "name")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "india_price")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "US_price")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "percent_difference")
      .head
      .dataType shouldBe StringType
  }

}
