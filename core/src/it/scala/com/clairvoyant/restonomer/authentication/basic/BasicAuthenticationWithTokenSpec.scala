package com.clairvoyant.restonomer.authentication.basic

import com.clairvoyant.restonomer.MockedHttpServer
import com.clairvoyant.restonomer.core.app.RestonomerContext
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BasicAuthenticationWithTokenSpec extends AnyFlatSpec with Matchers with MockedHttpServer {
  override val mappingsDirectory: String = "authentication/basic"

  val restonomerContext: RestonomerContext = RestonomerContext("core/src/it/resources/restonomer_context")

  it should "authenticate request with basic authentication using user name and password" in {
    restonomerContext.runCheckpoint("checkpoint_basic_authentication_up")
  }

}
