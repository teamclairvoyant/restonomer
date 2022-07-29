package com.clairvoyant.restonomer.core.backend

import com.clairvoyant.restonomer.core.model.config.{CredentialConfig, RequestConfig}

abstract class RestonomerBackend {
  type RequestR

  val httpRequest: RequestR

  def authenticate(credentialConfig: CredentialConfig): RequestR

  def send(httpRequest: RequestR, requestConfig: RequestConfig): RestonomerResponse
}

object RestonomerBackend {
  def apply() = new SttpBackend
}
