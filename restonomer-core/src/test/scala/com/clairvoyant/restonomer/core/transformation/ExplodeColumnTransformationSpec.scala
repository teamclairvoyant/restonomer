package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ExplodeColumnTransformationSpec extends CoreSpec with DataFrameMatchers {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession,
      text =
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": ["val_1", "val_2", "val_3"]
          |}
          |""".stripMargin
    ).read

  "transform() - with valid column name" should "explodeColumn the column into multiple rows" in {
    val restonomerTransformation = ExplodeColumn(
      columnName = "col_B"
    )

    val expectedRestonomerResponseTransformedDF = Seq(
      ("val_A", "val_1"),
      ("val_A", "val_2"),
      ("val_A", "val_3")
    )
      .toDF("col_A", "col_B")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
