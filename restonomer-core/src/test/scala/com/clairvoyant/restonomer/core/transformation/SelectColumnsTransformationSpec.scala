package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.DataFrame

class SelectColumnsTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "transform() - with column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = SelectColumns(
      columnNames = List("col_C", "col_B")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame = readJSON(
      """
        |{
        |  "col_B": "val_B",
        |  "col_C": "val_C"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
