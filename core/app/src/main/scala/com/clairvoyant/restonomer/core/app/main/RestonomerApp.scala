package com.clairvoyant.restonomer.core.app.main

import com.clairvoyant.restonomer.core.app.context.RestonomerContext

object RestonomerApp extends App {
  println("===== Welcome to Restonomer App =====")

  val restonomerContext = RestonomerContext()

  // RestonomerRequestWithoutAuthentication
  restonomerContext.runCheckpoint(checkpointName = "checkpoint_no_authentication")

  // RestonomerRequestWithBasicAuthenticationUsingUserNameAndPassword
  restonomerContext.runCheckpoint(checkpointName = "checkpoint_basic_authentication_up")

  // RestonomerRequestWithBasicAuthenticationUsingToken
  restonomerContext.runCheckpoint(checkpointName = "checkpoint_basic_authentication_token")
}
