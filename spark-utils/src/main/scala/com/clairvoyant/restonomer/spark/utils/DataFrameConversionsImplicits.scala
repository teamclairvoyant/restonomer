package com.clairvoyant.restonomer.spark.utils

import com.clairvoyant.restonomer.spark.utils.StringHelper.{hasUmlaut, sanitiseColumnName}
import org.apache.spark.sql.DataFrame

object DataFrameConversionsImplicits {

  implicit class DataFrameWrapper(dataFrame: DataFrame) {

    def sanitised: DataFrame = {
      val modifiedDF =
        dataFrame.columns
          .foldLeft(dataFrame) {
            case (df, columnName) if columnName.startsWith("_") =>
              df.withColumnRenamed(columnName, columnName.replaceFirst("_", "index_"))
            case (df, _) =>
              df
          }

      val umlautExist = modifiedDF.columns.exists(hasUmlaut)

      if (umlautExist) {
        modifiedDF.columns.foldLeft(modifiedDF) { case (df, columnName) =>
          df.withColumnRenamed(columnName, sanitiseColumnName(columnName))
        }
      } else
        modifiedDF
    }

  }

}
