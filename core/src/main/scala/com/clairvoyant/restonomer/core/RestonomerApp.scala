package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object RestonomerApp extends App {
  RestonomerContext().runCheckpoint(checkpointFilePath = "checkpoint_no_authentication.conf")
}
