package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ExplodeColumnTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": ["val_1", "val_2", "val_3"]
          |}
          |""".stripMargin
      )
    )

  "transform() - with valid column name" should "explodeColumn the column into multiple rows" in {
    val restonomerTransformation = ExplodeColumn(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |[
            |  {
            |    "col_A": "val_A",
            |    "col_B": "val_1"
            |  },
            |  {
            |    "col_A": "val_A",
            |    "col_B": "val_2"
            |  },
            |  {
            |    "col_A": "val_A",
            |    "col_B": "val_3"
            |  }
            |]
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
