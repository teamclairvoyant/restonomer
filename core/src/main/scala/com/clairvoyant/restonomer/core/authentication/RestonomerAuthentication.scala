package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import sttp.client3.Request

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

case class BasicAuthentication(basicToken: Option[String], userName: Option[String], password: Option[String])
    extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (basicToken.isEmpty && userName.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain either basicToken or both user-name & password."
      )
    else if (basicToken.isEmpty && userName.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain the user-name."
      )
    else if (basicToken.isEmpty && password.isEmpty)
      throw new RestonomerContextException(
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

case class BearerAuthentication(bearerToken: Option[String]) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (bearerToken.isEmpty)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain bearer-token."
      )
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {
    httpRequest.auth.bearer(bearerToken.get)
  }

}
