package com.clairvoyant.restonomer

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, Suite}

import java.io.File
import java.nio.file.Paths

trait MockedHttpServer extends BeforeAndAfterAll {
  this: Suite =>

  val mockedDataRootDirectory: String = new File("core/src/it/resources/mock_data").getAbsolutePath

  val mappingsDirectory: String

  lazy val mockedHttpServer: WireMockServer = {
    val wireMockServer =
      new WireMockServer(
        wireMockConfig()
          .dynamicPort()
          .usingFilesUnderDirectory(Paths.get(mockedDataRootDirectory, mappingsDirectory).toString)
      )

    wireMockServer.start()
    wireMockServer
  }

  override def beforeAll(): Unit = super.beforeAll()

  override def afterAll(): Unit = {
    super.afterAll()
    mockedHttpServer.stop()
  }

}
