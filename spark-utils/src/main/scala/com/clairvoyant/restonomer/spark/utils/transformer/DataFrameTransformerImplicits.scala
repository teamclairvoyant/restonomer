package com.clairvoyant.restonomer.spark.utils.transformer

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, lit}

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

    def drop(columnNames: Set[String]): DataFrame = df.drop(columnNames.toList: _*)

    def explode(columnName: String): DataFrame =
      df.withColumn(columnName, org.apache.spark.sql.functions.explode(col(columnName)))

  }

}
