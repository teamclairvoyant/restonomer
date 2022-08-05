package com.clairvoyant.restonomer.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, Suite}

trait HttpMockSpec extends BeforeAndAfterAll {
  this: Suite =>

  val wireMockServer = new WireMockServer(wireMockConfig().port(8080))

  override def beforeAll(): Unit = {
    wireMockServer.start()
  }

  override def afterAll(): Unit = {
    wireMockServer.stop()
  }

}
