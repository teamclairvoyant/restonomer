package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.APIKeyPlaceholders
import com.clairvoyant.restonomer.core.common.APIKeyPlaceholders.{isValidAPIKeyPlaceholder, COOKIE, QUERY_STRING, REQUEST_HEADER}
import com.clairvoyant.restonomer.core.exception.RestonomerContextException
import sttp.client3.{Identity, Request}
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import java.time.Clock

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
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain either basic-token or both user-name & password."
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

case class JwtAuthentication(
    subject: String,
    secretKey: String,
    algo: Option[String] = None
) extends RestonomerAuthentication {

  implicit val clock: Clock = Clock.systemUTC
  var encodingAlgo:JwtAlgorithm = _

  override def validateCredentials(): Unit = {
    if (subject.isBlank || secretKey.isBlank)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain subject and secret-key."
      )
  }

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] = {

    if (algo.isEmpty) {
      encodingAlgo = JwtAlgorithm.HS256
    }
    else {
      encodingAlgo =
        algo.get match {
          case "HMD5" =>
            JwtAlgorithm.HMD5
          case "HS224" =>
            JwtAlgorithm.HS224
          case "HS256" =>
            JwtAlgorithm.HS256
          case "HS384" =>
            JwtAlgorithm.HS384
          case "HS512" =>
            JwtAlgorithm.HS512
          case "RS256" =>
            JwtAlgorithm.RS256
          case "RS384" =>
            JwtAlgorithm.RS384
          case "RS512" =>
            JwtAlgorithm.RS512
          case "ES256" =>
            JwtAlgorithm.ES256
          case "ES384" =>
            JwtAlgorithm.ES384
          case "ES512" =>
            JwtAlgorithm.ES512
          case "Ed25519" =>
            JwtAlgorithm.Ed25519
          case default =>
            JwtAlgorithm.HS256
        }
    }
    val sub = subject
    val token = Jwt.encode(JwtClaim(s"""{"subject":"$sub"}""").issuedNow.expiresIn(1800), secretKey, encodingAlgo)
    val headers = Map("authorization" -> s"Bearer $token")
    httpRequest.headers(headers)
  }

}

case class BearerAuthentication(bearerToken: String) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (bearerToken.isBlank)
      throw new RestonomerContextException(
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
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain valid api-key-name."
      )
    else if (apiKeyValue.isBlank)
      throw new RestonomerContextException(
        "The provided credentials are invalid. The credentials should contain valid api-key-value."
      )
    else if (!isValidAPIKeyPlaceholder(placeholder))
      throw new RestonomerContextException(
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
