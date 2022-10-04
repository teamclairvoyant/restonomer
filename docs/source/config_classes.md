# Checkpoint Config

The checkpoint configuration is the entry point for the restonomer framework.
The restonomer framework expects you to provide checkpoint configurations to the restonomer context instance in order to trigger a checkpoint.

The checkpoint configuration is represented by `CheckpointConfig` class:

```scala
case class CheckpointConfig(
    name: String,
    request: RequestConfig,
    response: ResponseConfig,
    httpBackendType: String = HttpBackendTypes.HTTP_CLIENT_SYNC_BACKEND.toString
)
```

The checkpoint configuration contains below configs options to be provided by the user:

| Config Name     | Mandatory |      Default Value      | Description                                                                                                       |
|:----------------|:---------:|:-----------------------:|:------------------------------------------------------------------------------------------------------------------|
| name            |    Yes    |            -            | Unique name for your checkpoint                                                                                   |
| request         |    Yes    |            -            | Request configuration represented by `RequestConfig`                                                              |
| response        |    Yes    |            -            | Response configuration represented by `ResponseConfig`                                                            |
 | httpBackendType |    No     | `HttpClientSyncBackend` | Synchronous/Asynchronous backend that take care of managing connections, sending requests and receiving responses |

User can provide checkpoint configuration file in HOCON format in the below format:

```hocon
name = "sample_postman_checkpoint"

request = {
  url = "https://postman-echo.com/basic-auth"
  
  authentication = {
    type = "basic-authentication"
    user-name = "postman"
    password = "password"
  }
}

response = {
  body = {
    format = "JSON"
  }
}

http-backend-type = "HttpClientSyncBackend"
```

# Request Config

The basic http request configurations are represented by `RequestConfig` class:

```scala
case class RequestConfig(
    method: String = Method.GET.method,
    url: String,
    authentication: Option[RestonomerAuthentication] = None,
    headers: Map[String, String] = Map[String, String]().empty
)
```

The request configuration contains below configs options to be provided by the user:

| Config Name    | Mandatory | Default Value | Description                                                                                                                                                                             |
|:---------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| method         |    No     |     `GET`     | Http request method                                                                                                                                                                     |
| url            |    Yes    |       -       | Url for the REST API request                                                                                                                                                            |
| authentication |    Yes    |       -       | The type of authentication mechanism supported by Http Request<br/>Restonomer supports: `basic-authentication`, `bearer-authentication`, `api-key-authentication`, `jwt-authentication` |
| headers        |    No     |       -       | List of headers to provided as a part of Http request in the form of key-value pairs                                                                                                    |

The request configuration can be represented in the checkpoint file in below manner:

```hocon
request = {
  method = "GET"
 
  url = "http://localhost:8080/custom-headers"

  authentication = {
    type = "basic-authentication"
    user-name = "test_user"
    password = "test_password"
  }
 
  headers = {
    "header_key_1" = "header_value_1",
    "header_key_2" = "header_value_2"
  }
}
```
# Authentication Config

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

# Response Config

In restonomer framework, the details about the http response (like response body format) are captured via class 
`ResponseConfig`:

```scala
case class ResponseConfig(
    body: ResponseBodyConfig
)
```

User need to provide below configs for Response Configuration:

| Config Name | Mandatory | Default Value | Description                                                       |
|:------------|:---------:|:-------------:|:------------------------------------------------------------------|
| body        |    Yes    |       -       | The body of the http response represented by `ResponseBodyConfig` |

The response configurations are provided in the checkpoint file in the below manner:

```hocon
response = {
  body = {
    format = "JSON"
  }
}
```

# Response Body Config

The configurations related to the body of the http response are represented by class `ResponseBodyConfig`:

```scala
case class ResponseBodyConfig(
    format: String
)
```

User need to provide below configs for Response Body Configuration:

| Config Name | Mandatory | Default Value | Description                                                                                                                                                                                      |
|:------------|:---------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| format      |    Yes    |       -       | The format of the body of the http response<br/>Restonomer framework supports handling formats like `JSON`<br/>Restonomer uses this information internally to convert response body to dataframe |

The response configurations are provided in the checkpoint file in the below manner:

```hocon
body = {
  format = "JSON"
}
```