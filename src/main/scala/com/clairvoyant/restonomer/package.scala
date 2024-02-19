package com.clairvoyant

import sttp.client3.{HttpClientFutureBackend, Request, Response, ResponseAs, SttpBackend}

import scala.concurrent.Future

package object restonomer {
  val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()

  type HttpRequest[T] = Request[Either[String, T], Any]
  type HttpResponseAs[T] = ResponseAs[Either[String, T], Any]
  type HttpResponse[T] = Response[Either[String, T]]
  type HttpResponseBody[T] = Seq[T]
}
