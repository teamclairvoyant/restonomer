package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class CastNestedColumnTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": {
      |     "col_C": "val_C",
      |     "col_D": 5
      |  }
      |}
      |""".stripMargin
  )

  "transform() - with valid column name and ddl" should "cast the nested column" in {
    restonomerResponseDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .toDDL shouldBe "col_B STRUCT<col_C: STRING, col_D: BIGINT>"

    val restonomerTransformation = CastNestedColumn(
      columnName = "col_B",
      ddl = "col_C STRING, col_D STRING"
    )

    val restonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    restonomerResponseTransformedDF.schema.fields
      .filter(_.name == "col_B")
      .head
      .toDDL shouldBe "col_B STRUCT<col_C: STRING, col_D: STRING>"
  }

}
