package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.backend.RestonomerBackend
import com.clairvoyant.restonomer.core.model.config.CredentialConfig

class BasicAuthentication(restonomerBackend: RestonomerBackend, credentialConfig: CredentialConfig)
    extends RestonomerAuthentication(restonomerBackend) {

  override def authenticate: restonomerBackend.RequestR = restonomerBackend.authenticate(credentialConfig)

}
