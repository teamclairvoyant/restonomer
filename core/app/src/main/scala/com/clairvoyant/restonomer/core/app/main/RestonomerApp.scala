package com.clairvoyant.restonomer.core.app.main

import com.clairvoyant.restonomer.core.app.context.RestonomerContext

object RestonomerApp extends App {
  RestonomerContext().runAllCheckpoints()
}
