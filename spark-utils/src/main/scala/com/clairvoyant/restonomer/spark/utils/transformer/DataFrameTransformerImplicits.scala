package com.clairvoyant.restonomer.spark.utils.transformer

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit

object DataFrameTransformerImplicits {

  implicit class DataFrameWrapper(df: DataFrame) {

    def addColumn(
        columnName: String,
        columnValue: String,
        columnDataType: Option[String]
    ): DataFrame =
      columnDataType
        .map(dataType => df.withColumn(columnName, lit(columnValue).cast(dataType)))
        .getOrElse(df.withColumn(columnName, lit(columnValue)))

    def dropColumns(
        columnNames: Set[String]
    ): DataFrame = df.drop(columnNames.toList: _*)

  }

}
