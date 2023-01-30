package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DecimalType, DoubleType, LongType, StringType}

class CastColumnsTransformationSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": 5,
          |  "col_B": 4,
          |  "col_C": 3.4678
          |}
          |""".stripMargin
      )
    )

  "transform() - with columnDataTypeMapper" should "cast columns as specified in the mapper" in {
    restonomerResponseDF.schema.fields
      .filter(_.name == "col_A")
      .head
      .dataType shouldBe LongType

    restonomerResponseDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .dataType shouldBe LongType

    restonomerResponseDF.schema.fields
      .filter(_.name == "col_C")
      .head
      .dataType shouldBe DoubleType

    val restonomerTransformation = CastColumns(
      columnDataTypeMapper = Map(
        "col_A" -> "string",
        "col_B" -> "double",
        "col_C" -> "decimal(19, 2)"
      )
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_A")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .dataType shouldBe DoubleType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_C")
      .head
      .dataType shouldBe DecimalType(19, 2)
  }

}
