package com.clairvoyant.restonomer.common

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, Suite}

trait MockedHttpServer extends BeforeAndAfterAll {
  this: Suite =>

  val resourcesDirectoryPath = "restonomer-core/src/it/resources"
  val mockDataRootDirectoryPath: String = s"$resourcesDirectoryPath/mock_data"

  val mappingsDirectory: String

  lazy val mockedHttpServer: WireMockServer =
    new WireMockServer(
      wireMockConfig()
        .port(8080)
        .usingFilesUnderDirectory(s"$mockDataRootDirectoryPath/$mappingsDirectory")
    )

  override def beforeAll(): Unit = mockedHttpServer.start()
  override def afterAll(): Unit = mockedHttpServer.stop()
}
