package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.CoreSpec
import com.clairvoyant.restonomer.core.authentication.BasicAuthentication
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class RestonomerRequestBuilderSpec extends CoreSpec {

  "withAuthentication - without authenticationConfig" should "return RestonomerRequestBuilder object with the same httpRequest" in {
    val authentication = None
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest should be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "withAuthentication - with authenticationConfig" should "return RestonomerRequestBuilder object with the new authenticated httpRequest" in {
    val authentication = Some(BasicAuthentication(basicToken = Some("test_token")))
    val restonomerRequestBuilder = RestonomerRequestBuilder(basicHttpRequest)

    restonomerRequestBuilder.withAuthentication(authentication) shouldBe a[RestonomerRequestBuilder]

    restonomerRequestBuilder
      .withAuthentication(authentication)
      .httpRequest shouldNot be theSameInstanceAs restonomerRequestBuilder.httpRequest
  }

  "build" should "return RestonomerRequestObject" in {
    RestonomerRequestBuilder(basicHttpRequest).build shouldBe a[RestonomerRequest]
  }

}
