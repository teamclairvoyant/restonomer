package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ConvertColumnToJsonTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |    "col_A": "1",
          |    "col_B": [
          |        {
          |            "Zipcode": 704,
          |            "ZipCodeType": "STANDARD"
          |        }
          |    ]
          |}""".stripMargin
      )
    )

  "transform() - with valid column name" should "transform the column to json" in {
    val restonomerTransformation = ConvertColumnToJson(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |    "col_A": "1",
            |    "col_B": "[{\"ZipCodeType\":\"STANDARD\",\"Zipcode\":704}]"
            |}""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
