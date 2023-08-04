package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class AddMissingColumnsTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": "val_B",
          |  "col_C": "val_C"
          |}
          |""".stripMargin
      )
    )
  
  "transform() - with details of missing columns" should "Add the columns" in {
    val restonomerTransformation = AddMissingColumns(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = "String"
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
            |  "col_C": "val_C",
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


  "tranform() - if columns are already present"  should "return the original dataframe" in {
    val restonomerTransformation = AddMissingColumns(
       columnName = "col_A",
       columnValue = "val_A",
       columnDataType = "String"
    )

    val expectedRestonomerResponseTransformedDF = restonomerResponseDF

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )


 }
}


