package com.clairvoyant.restonomer.spark.utils

import org.apache.spark.sql.DataFrame
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.should.Matchers

trait DataFrameMatchers {
  self: Matchers =>

  def matchExpectedDataFrame(
      expectedDF: DataFrame,
      expectedSchemaTransformer: DataFrameSchemaTransformer
  ): Matcher[DataFrame] =
    (actualDF: DataFrame) => {
      import com.clairvoyant.restonomer.spark.utils.DataFrameConversionsImplicits._

      val expectedTransformedDF = expectedSchemaTransformer(expectedDF.sanitised)
    }

}
