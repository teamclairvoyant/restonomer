package com.clairvoyant.restonomer.core.http.request

import com.clairvoyant.restonomer.core.authentication.RestonomerAuthentication
import com.clairvoyant.restonomer.core.model.config.RequestConfig

class BasicRequestWithAuthentication(requestConfig: RequestConfig) extends BasicRequest(requestConfig) {

  override val restonomerAuthentication: Option[RestonomerAuthentication] = requestConfig.authentication.map(
    RestonomerAuthentication(_)
  )

  override def authenticate: BasicRequestWithAuthentication = restonomerAuthentication.
}
