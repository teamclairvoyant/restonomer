package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.core.common.CoreSpec

class CSVResponseToDataFrameConverterSpec extends CoreSpec {

  "convertResponseToDataFrame() - with csv response body" should "return a dataframe with correct count and schema" in {
    val responseBody =
      """col1,col2
        |1,2
        |3,4""".stripMargin

    val expectedSchema = "struct<col1:string,col2:string>"

    val df = new CSVResponseToDataFrameConverter().convertResponseToDataFrame(Seq(responseBody))

    df.count() shouldBe 2
    df.schema.simpleString shouldBe expectedSchema
  }

}
