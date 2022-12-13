package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.common.{CoreSpec, HttpMockSpec}
import com.clairvoyant.restonomer.core.model.RequestConfig
import sttp.client3._
import sttp.model.Method

class RestonomerRequestSpec extends CoreSpec with HttpMockSpec {

  "builder" should "return RestonomerRequestBuilder object" in {
    val requestConfig = RequestConfig(
      method = Method.GET,
      url = uri"https://test-domain/url"
    )

    RestonomerRequest.builder(requestConfig) shouldBe a[RestonomerRequestBuilder]
    RestonomerRequest.builder(requestConfig).httpRequest shouldBe a[Request[_, _]]
  }

}
