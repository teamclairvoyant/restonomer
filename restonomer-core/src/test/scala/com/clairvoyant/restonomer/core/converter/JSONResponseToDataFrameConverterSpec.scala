package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.core.common.CoreSpec

class JSONResponseToDataFrameConverterSpec extends CoreSpec {

  "convertResponseToDataFrame() - with json response body" should "return a dataframe with correct count and schema" in {
    val responseBody =
      """{
        |  "affiliate_network_id": "adcde51",
        |  "transaction_id": "19429637",
        |  "time_of_event": "2019-04-01 15:52:10"
        |}""".stripMargin

    val expectedSchema = "struct<affiliate_network_id:string,time_of_event:string,transaction_id:string>"

    val df = new JSONResponseToDataFrameConverter().convertResponseToDataFrame(responseBody)

    df.count() shouldBe 1
    df.schema.simpleString shouldBe expectedSchema
  }

}
