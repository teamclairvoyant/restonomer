package com.clairvoyant.restonomer.transformation

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import org.apache.spark.sql.AnalysisException

class FilterByRegexTransformationSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |[
      |  {
      |    "col_A": "10",
      |    "col_B": "abc@gmail.com"
      |  },
      |  {
      |    "col_A": "20",
      |    "col_B": "def@yahoo.com"
      |  }
      |]
      |""".stripMargin
  )

  "transform() - with valid condition" should "filter the dataframe as expected" in {
    val restonomerTransformation = FilterByRegex(columnName = "col_B", regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b")

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |[
        |  {
        |    "col_A": "10",
        |    "col_B": "abc@gmail.com"
        |  }
        |]
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
