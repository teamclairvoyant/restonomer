package com.clairvoyant.restonomer

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, Suite}

trait MockedHttpServer extends BeforeAndAfterAll {
  this: Suite =>

  lazy val mockedHttpServer: WireMockServer =
    new WireMockServer(
      wireMockConfig()
        .port(8080)
        .usingFilesUnderDirectory(s"$mockDataRootDirectoryPath/$mappingsDirectory")
    )
  val mockDataRootDirectoryPath: String = "core/src/it/resources/mock_data"
  val mappingsDirectory: String

  override def beforeAll(): Unit = mockedHttpServer.start()
  override def afterAll(): Unit = mockedHttpServer.stop()
}
