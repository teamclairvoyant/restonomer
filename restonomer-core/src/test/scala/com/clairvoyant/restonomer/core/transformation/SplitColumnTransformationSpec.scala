package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class SplitColumnTransformationSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(
      text = Seq(
        """
          |{
          | "address": "Apt-123,XYZ Building,Pune,Maharashtra"
          |}
          |""".stripMargin
      )
    )

  "transform()" should "split the column and create new columns accordingly" in {
    val restonomerTransformation = SplitColumn(
      fromColumn = "address",
      delimiter = ",",
      toColumns = Map(
        "apt_number" -> 0,
        "society_name" -> 1,
        "city" -> 2,
        "state" -> 3
      )
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            | "address": "Apt-123,XYZ Building,Pune,Maharashtra",
            | "apt_number": "Apt-123",
            | "society_name": "XYZ Building",
            | "city": "Pune",
            | "state": "Maharashtra"
            |}
            |""".stripMargin
        )
      )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedRestonomerResponseTransformedDF)
  }

}
