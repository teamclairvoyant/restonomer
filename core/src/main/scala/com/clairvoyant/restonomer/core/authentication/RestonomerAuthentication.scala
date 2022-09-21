package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.APIKeyPlaceholders
import com.clairvoyant.restonomer.core.common.APIKeyPlaceholders.{isValidAPIKeyPlaceholder, COOKIE, QUERY_STRING, REQUEST_HEADER}
import com.clairvoyant.restonomer.core.exception.RestonomerException
import sttp.client3.{Identity, Request}

sealed trait RestonomerAuthentication {

  def validateCredentials(): Unit

  def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any]

  def validateCredentialsAndAuthenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
    validateCredentials()
    authenticate(httpRequest)
  }

}

case class BasicAuthentication(
    basicToken: Option[String] = None,
    userName: Option[String] = None,
    password: Option[String] = None
) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (basicToken.isEmpty && userName.isEmpty && password.isEmpty)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain either basic-token or both user-name & password."
      )
    else if (basicToken.isEmpty && userName.isEmpty)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain the user-name."
      )
    else if (basicToken.isEmpty && password.isEmpty)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain the password."
      )
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    basicToken
      .map(httpRequest.auth.basicToken)
      .getOrElse(
        httpRequest.auth.basic(
          user = userName.get,
          password = password.get
        )
      )
  }

}

case class BearerAuthentication(bearerToken: String) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (bearerToken.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid bearer-token."
      )
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    httpRequest.auth.bearer(bearerToken)
  }

}

case class APIKeyAuthentication(apiKeyName: String, apiKeyValue: String, placeholder: String)
    extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (apiKeyName.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid api-key-name."
      )
    else if (apiKeyValue.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid api-key-value."
      )
    else if (!isValidAPIKeyPlaceholder(placeholder))
      throw new RestonomerException(
        s"The provided credentials are invalid. The placeholder: $placeholder is not supported."
      )
  }

  override def authenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
    APIKeyPlaceholders(placeholder) match {
      case QUERY_STRING =>
        httpRequest.copy[Identity, Either[String, String], Any](
          uri = httpRequest.uri.addParam(apiKeyName, apiKeyValue)
        )
      case REQUEST_HEADER =>
        httpRequest.header(apiKeyName, apiKeyValue, replaceExisting = true)
      case COOKIE =>
        httpRequest.cookie(apiKeyName, apiKeyValue)
    }
  }

}
