# What is token request ?

The authentication mechanism in restonomer also gives the user a provision to configure the token request.

There are certain scenarios where you are required to provide the authentication credentials for the main data request
from the token value provided by some other token request.

Keeping that in mind, user can configure the token request and then can use the token response in the main data request.

## Placeholders for token response

User can configure whether the token value needs to be fetched from token response body or token response headers.

The `token-response-placeholder` config in `token-request` can be initialised with below 2 values:
* `ResponseBody`
* `ResponseHeaders`

**_If the token is to be fetched from the token response body, it is assumed that the token request will always generate
the token response in a json format that will contain the attribute required by the main data request._**

## Token request configuration

The token request can be configured in the `token` section of the checkpoint configuration file in below manner:

```hocon
token = {
  token-request = {
    url = "http://localhost:8080/token-response-body"
  }

  token-response-placeholder = "ResponseBody"
}
```

The `token-request` configuration contains below config options to be provided by the user:

| Config Name    | Mandatory | Default Value | Description                                                                                                    |
|:---------------|:---------:|:-------------:|:---------------------------------------------------------------------------------------------------------------|
| method         |    No     |     `GET`     | Http request method                                                                                            |
| url            |    Yes    |       -       | Url for the REST API request                                                                                   |
| query-params   |    No     |       -       | The map of query parameters                                                                                    |
| authentication |    No     |       -       | The type of authentication mechanism supported by Http Request represented by `RestonomerAuthentication` class |
| body           |    No     |       -       | The request body represented by `RestonomerRequestBody` body to be send along with the request                 |
| headers        |    No     |       -       | List of headers to provided as a part of Http request in the form of key-value pairs                           |
| retry          |    No     |       -       | The auto retry configuration represented by `RetryConfig` class                                                |
