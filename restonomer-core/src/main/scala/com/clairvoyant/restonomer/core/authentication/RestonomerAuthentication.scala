package com.clairvoyant.restonomer.core.authentication

import com.clairvoyant.restonomer.core.common.*
import com.clairvoyant.restonomer.core.common.APIKeyPlaceholders.*
import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.sttpBackend
import com.jayway.jsonpath.JsonPath
import pdi.jwt.*
import pdi.jwt.algorithms.JwtUnknownAlgorithm
import sttp.client3.*
import zio.config.derivation.nameWithLabel

import java.time.Clock
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@nameWithLabel
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

  override def authenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
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

case class BearerAuthentication(
    bearerToken: String
) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (bearerToken.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid bearer-token."
      )
  }

  override def authenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = httpRequest.auth.bearer(bearerToken)

}

case class APIKeyAuthentication(
    apiKeyName: String,
    apiKeyValue: String,
    placeholder: String
) extends RestonomerAuthentication {

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
      case QUERY_PARAM =>
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

case class JWTAuthentication(
    subject: String,
    secretKey: String,
    algorithm: String = JwtAlgorithm.HS256.name,
    tokenExpiresIn: Long = 1800
) extends RestonomerAuthentication {

  given clock: Clock = Clock.systemDefaultZone()

  override def validateCredentials(): Unit = {
    if (subject.isBlank || secretKey.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain both subject and secret-key."
      )

    JwtAlgorithm.fromString(algorithm) match {
      case JwtUnknownAlgorithm(algorithm) =>
        throw new RestonomerException(s"The provided algorithm: $algorithm is not supported.")
      case _ =>
    }
  }

  override def authenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
    httpRequest.auth.bearer(
      Jwt.encode(
        claim = JwtClaim(subject = Option(subject)).issuedNow.expiresIn(tokenExpiresIn),
        key = secretKey,
        algorithm = JwtAlgorithm.fromString(algorithm)
      )
    )
  }

}

case class DigestAuthentication(
    userName: String,
    password: String
) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = {
    if (userName.isBlank && password.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain both user-name & password."
      )
    else if (userName.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid user-name."
      )
    else if (password.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid password."
      )
  }

  override def authenticate(
      httpRequest: Request[Either[String, String], Any]
  ): Request[Either[String, String], Any] = {
    httpRequest.auth.digest(
      user = userName,
      password = password
    )
  }

}

case class OAuth2Authentication(
    grantType: OAuth2AuthenticationGrantType
) extends RestonomerAuthentication {

  override def validateCredentials(): Unit = grantType.validateCredentials()

  override def authenticate(httpRequest: Request[Either[String, String], Any]): Request[Either[String, String], Any] =
    httpRequest.auth.bearer(grantType.getAccessToken)

}

@nameWithLabel(keyName = "name")
sealed trait OAuth2AuthenticationGrantType {
  def validateCredentials(): Unit
  def getAccessToken: String
}

case class ClientCredentials(
    tokenUrl: String,
    clientId: String,
    clientSecret: String
) extends OAuth2AuthenticationGrantType {

  override def validateCredentials(): Unit =
    if (tokenUrl.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. Please provide token-url."
      )
    else if (clientId.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid client-id."
      )
    else if (clientSecret.isBlank)
      throw new RestonomerException(
        "The provided credentials are invalid. The credentials should contain valid client-secret."
      )

  override def getAccessToken: String =
    JsonPath.read[String](
      Await
        .result(
          basicRequest
            .post(uri"$tokenUrl")
            .auth
            .basic(clientId, clientSecret)
            .body(Map("grant_type" -> "client_credentials"))
            .send(sttpBackend),
          Duration.Inf
        )
        .body match {
        case Left(errorMessage) =>
          throw new RestonomerException(errorMessage)
        case Right(responseBody) =>
          responseBody
      },
      "$.access_token"
    )

}
