package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{StringType, TimestampType}

class ConvertColumnToTimestampTransformationSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(
      text = Seq(
        """
          |{
          | "time": "1990-07-23 10:30:20"
          |}
          |""".stripMargin
      )
    )

  "transform()" should "convert the column to timestamp type" in {
    restonomerResponseDF.schema.fields.head.dataType shouldBe StringType

    val restonomerTransformation = ConvertColumnToTimestamp(
      columnName = "time",
      timestampFormat = "yyyy-MM-dd HH:mm:ss"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields.head.dataType shouldBe TimestampType
  }

}
