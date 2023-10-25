package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import org.apache.spark.sql.types.{StringType, StructField, StructType}

class ConvertJSONStringToStructTransformationSpec extends DataFrameReader with DataFrameMatcher {

  "transform()" should "convert the specified column to Struct Type" in {

    val restonomerResponseDF = readJSONFromText(
      """
        |{
        |  "col_A": "{\"col_B\":\"val_B1\",\"col_C\":\"val_C1\"}"
        |}
        |""".stripMargin
    )

    val restonomerTransformation = ConvertJSONStringToStruct(
      columnName = "col_A"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |{
        |  "col_A": {
        |    "col_B": "val_B1",
        |    "col_C": "val_C1"
        |  }
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform()" should "convert the specified column to Struct Type based on given schema DDL" in {

    val restonomerResponseDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": "{\"col_C\": \"val_C\",\"col_D\": 5}"
        |}
        |""".stripMargin
    )

    val restonomerTransformation = ConvertJSONStringToStruct(
      columnName = "col_B",
      schemaDDL = Option("col_C STRING, col_D STRING")
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_A": "val_A",
        |  "col_B": {
        |    "col_C": "val_C",
        |    "col_D": "5"
        |  }
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .dataType shouldBe StructType(
      List(
        StructField("col_C", StringType),
        StructField("col_D", StringType)
      )
    )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

}
