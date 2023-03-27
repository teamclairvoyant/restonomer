package com.clairvoyant.restonomer

import sttp.client3.{HttpClientFutureBackend, SttpBackend}

import scala.concurrent.Future

package object core {
  val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()
}
