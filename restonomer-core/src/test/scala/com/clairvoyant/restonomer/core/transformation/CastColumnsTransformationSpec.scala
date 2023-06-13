package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.*

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
          |  "col_C": 3.4678,
          |  "col_D": "1990-07-23 10:20:30",
          |  "col_E": "23-07-1990 10:20:30",
          |  "col_F": "1990-07-23",
          |  "col_G": "23-07-1990"
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
        "col_C" -> "decimal(19, 2)",
        "col_D" -> "timestamp",
        "col_E" -> "timestamp(dd-MM-yyyy HH:mm:ss)",
        "col_F" -> "date",
        "col_G" -> "date(dd-MM-yyyy)"
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

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_D")
      .head
      .dataType shouldBe TimestampType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_E")
      .head
      .dataType shouldBe TimestampType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_F")
      .head
      .dataType shouldBe DateType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_G")
      .head
      .dataType shouldBe DateType
  }

}
