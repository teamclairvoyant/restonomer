package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.expr

class AddColumnTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": "val_B",
          |  "col_C": 10.2
          |}
          |""".stripMargin
      )
    )

  "transform() - with column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      valueType = "literal",
      columnDataType = Some("string")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": 10.2,
            |  "col_D": "val_D"
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - without column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      valueType = "literal",
      columnDataType = None
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": 10.2,
            |  "col_D": "val_D"
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with int column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "1",
      valueType = "literal",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": 10.2,
            |  "col_D": 1
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with expression and without casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "col_c*2",
      valueType = "expression"
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": 10.2,
            |  "col_D": 20.4
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)
    actualRestonomerResponseTransformedDF.show(10, false)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with expression and casting" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "col_c*2",
      valueType = "expression",
      columnDataType = Some("long")
    )

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "col_A": "val_A",
            |  "col_B": "val_B",
            |  "col_C": 10.2,
            |  "col_D": 20
            |}
            |""".stripMargin
        )
      )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
