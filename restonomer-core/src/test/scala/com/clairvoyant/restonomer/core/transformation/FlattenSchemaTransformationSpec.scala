package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.spark.sql.DataFrame

class FlattenSchemaTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "rewardApprovedMonthPeriod": {
      |      "from": "2021-09",
      |      "to": "2021-10"
      |   }
      |}
      |""".stripMargin
  )

  "transform()" should "flatten the response dataframe" in {
    val restonomerTransformation = FlattenSchema()

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    val expectedRestonomerResponseTransformedDF = readJSON(
      """
        |{
        |  "rewardApprovedMonthPeriod_from": "2021-09",
        |  "rewardApprovedMonthPeriod_to": "2021-10"
        |}
        |""".stripMargin
    )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedRestonomerResponseTransformedDF)
  }

}
