The authentication in restonomer is represented by trait `RestonomerAuthentication`:

```scala
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
```

The restonomer framework supports below authentication mechanisms:

* Basic Authentication
* Bearer Authentication
* API Key Authentication
* JWT Authentication

## Basic Authentication

The basic authentication in restonomer framework is represented by class `BasicAuthentication`:

```scala
case class BasicAuthentication(
    basicToken: Option[String] = None,
    userName: Option[String] = None,
    password: Option[String] = None
) extends RestonomerAuthentication
```

Basic authentication can be achieved in restonomer using 2 ways:

* By providing username and password

```hocon
authentication = {
 type = "basic-authentication"
 user-name = "test_user"
 password = "test_password"
}
```

* By providing basic auth token

```hocon
authentication = {
 type = "basic-authentication"
 basic-token = "abcd1234"
}
```

## Bearer Authentication

The bearer authentication in restonomer framework is represented by class `BearerAuthentication`:

```scala
case class BearerAuthentication(
  bearerToken: String
) extends RestonomerAuthentication
```

You would need to provide just the bearer auth token to the `authentication` configuration in checkpoint:

```hocon
authentication = {
 type = "bearer-authentication"
 bearer-token = "abcd1234"
}
```

## API Key Authentication

The API key authentication in restonomer framework is represented by class `APIKeyAuthentication`:

```scala
case class APIKeyAuthentication(
    apiKeyName: String,
    apiKeyValue: String,
    placeholder: String
) extends RestonomerAuthentication
```

The API key authentication config expects user to provide below 3 details:

* name of api key
* value of api key
* placeholder that denotes where to the api key to the request (query param / header / cookies)

```hocon
authentication = {
  type = "api-key-authentication"
  api-key-name = "test_api_key_name"
  api-key-value = "test_api_key_value"
  placeholder = "QueryParam"
}
```

## JWT Authentication

The JWT authentication in restonomer framework is represented by class `JWTAuthentication`:

```scala
case class JWTAuthentication(
    subject: String,
    secretKey: String,
    algorithm: String = JwtAlgorithm.HS256.name,
    tokenExpiresIn: Long = 1800
) extends RestonomerAuthentication
```
