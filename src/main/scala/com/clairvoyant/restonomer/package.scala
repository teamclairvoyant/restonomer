package com.clairvoyant

import sttp.client3.{HttpClientFutureBackend, SttpBackend}

import scala.concurrent.Future

package object restonomer {
  val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()
}
