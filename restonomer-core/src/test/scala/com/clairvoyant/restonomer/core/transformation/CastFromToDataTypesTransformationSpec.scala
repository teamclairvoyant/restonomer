package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.*

class CastFromToDataTypesTransformationSpec extends CoreSpec {

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
          |  "col_D": {
          |     "col_E": 6
          |   },
          |  "col_F": [
          |    {
          |       "col_G": 7
          |    }
          |  ]
          |}
          |""".stripMargin
      )
    )

  "transform()" should "cast columns as per the from and to data types" in {
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

    val restonomerTransformation = CastFromToDataTypes(
      dataTypeMapper = Map(
        "long" -> "integer",
        "double" -> "decimal(5, 2)"
      ),
      castRecursively = true
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_A")
      .head
      .dataType shouldBe IntegerType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .dataType shouldBe IntegerType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_C")
      .head
      .dataType shouldBe new DecimalType(5, 2)
  }

}
