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


# Authentication Config

# Response Config

# Response Body Config