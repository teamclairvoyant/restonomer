package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import org.apache.spark.sql.DataFrame

class FlattenSchemaTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF = readJSONFromText(
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

    val expectedRestonomerResponseTransformedDF = readJSONFromText(
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
