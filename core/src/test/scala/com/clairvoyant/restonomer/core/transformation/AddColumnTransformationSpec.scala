package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row}

class AddColumnTransformationSpec extends CoreSpec with DataFrameMatchers {
  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C")).toDF("col_A", "col_B", "col_C")

  "transform() - with column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = Some("string")
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C", "val_D"))
      .toDF("col_A", "col_B", "col_C", "col_D")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - without column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "val_D",
      columnDataType = None
    )

    val expectedRestonomerResponseTransformedDF = Seq(("val_A", "val_B", "val_C", "val_D"))
      .toDF("col_A", "col_B", "col_C", "col_D")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with int column-data-type" should "transform the dataframe as expected" in {
    val restonomerTransformation = AddColumn(
      columnName = "col_D",
      columnValue = "1",
      columnDataType = Some("int")
    )

    val expectedRestonomerResponseTransformedDF = sparkSession.createDataFrame(
      sparkSession.sparkContext.parallelize(Seq(Row("val_A", "val_B", "val_C", 1))),
      StructType(
        Array(
          StructField("col_A", StringType, nullable = true),
          StructField("col_B", StringType, nullable = true),
          StructField("col_C", StringType, nullable = true),
          StructField("col_D", IntegerType, nullable = true)
        )
      )
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
