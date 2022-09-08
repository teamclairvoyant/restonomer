package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object RestonomerApp extends App {
  RestonomerContext().runAllCheckpoints()
//  RestonomerContext().runCheckpoint("checkpoint_bearer_authentication")

}
