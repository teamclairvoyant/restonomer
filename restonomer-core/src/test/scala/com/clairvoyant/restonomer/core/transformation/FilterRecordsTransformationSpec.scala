package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.AnalysisException

class FilterRecordsTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSONFromText(
    """
      |[
      |  {
      |    "col_A": "10",
      |    "col_B": "val_B1",
      |    "col_C": "val_C1"
      |  },
      |  {
      |    "col_A": "20",
      |    "col_B": "val_B2",
      |    "col_C": "val_C2"
      |  }
      |]
      |""".stripMargin
  )

  "transform() - with valid condition" should "filter the dataframe as expected" in {
    val restonomerTransformation = FilterRecords(
      filterCondition = "col_A > 10"
    )

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
      """
        |[
        |  {
        |    "col_A": "20",
        |    "col_B": "val_B2",
        |    "col_C": "val_C2"
        |  }
        |]
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

  "transform() - with invalid condition" should "throw AnalysisException" in {
    val restonomerTransformation = FilterRecords(
      filterCondition = "col_D > 10"
    )

    assertThrows[AnalysisException] {
      restonomerTransformation.transform(restonomerResponseDF)
    }
  }

}
