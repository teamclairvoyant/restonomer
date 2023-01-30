package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ConvertColumnToJsonTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession,
      text = Seq(
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
    ).read

  "transform() - with valid column name" should "transform the column to json" in {
    val restonomerTransformation = ConvertColumnToJson(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("1", """[{"ZipCodeType":"STANDARD","Zipcode":704}]"""))
      .toDF("col_A", "col_B")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
