package com.clairvoyant.restonomer.core.converter

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.core.exception.RestonomerException

class ResponseToDataFrameConverterSpec extends CoreSpec {

  "apply() method - with supported format" should "return proper ResponseToDataFrameConverter object" in {
    val format = "JSON"
    val responseToDataFrameConverter = ResponseToDataFrameConverter(format)

    responseToDataFrameConverter shouldBe a[JSONResponseToDataFrameConverter]
  }

  "apply() method - with non supported format" should "throw RestonomerException" in {
    val format = "JPEG"
    val thrown = the[RestonomerException] thrownBy ResponseToDataFrameConverter(format)

    thrown.getMessage should include(s"The response body format: $format is not supported.")
  }

}