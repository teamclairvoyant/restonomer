package com.clairvoyant.restonomer

import com.clairvoyant.restonomer.core.app.RestonomerContext
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait IntegrationTestDependencies extends AnyFlatSpec with Matchers with MockedHttpServer {

  val restonomerContext: RestonomerContext = RestonomerContext("core/src/it/resources/restonomer_context")

}
