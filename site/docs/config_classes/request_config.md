# RequestConfig

The basic http request configurations are represented by `RequestConfig` class.

The request configuration contains below config options to be provided by the user:

| Config Name    | Mandatory | Default Value | Description                                                                                                    |
|:---------------|:---------:|:-------------:|:---------------------------------------------------------------------------------------------------------------|
| method         |    No     |     `GET`     | Http request method                                                                                            |
| url            |    Yes    |       -       | Url for the REST API request                                                                                   |
| query-params   |    No     |       -       | The map of query parameters                                                                                    |
| authentication |    No     |       -       | The type of authentication mechanism supported by Http Request represented by `RestonomerAuthentication` class |
| body           |    No     |       -       | The request body represented by `RestonomerRequestBody` body to be send along with the request                 |
| headers        |    No     |       -       | List of headers to provided as a part of Http request in the form of key-value pairs                           |
| retry          |    No     |       -       | The auto retry configuration represented by `RetryConfig` class                                                |

The request configuration can be represented in the checkpoint file in below manner:

```hocon
data-request = {
  method = "GET"

  url = "http://localhost:8080/custom-headers"

  query-params = {
    "query_param_name_1" = "query_param_value_1",
    "query_param_name_2" = "query_param_value_2"
  }

  authentication = {
    type = "BasicAuthentication"
    user-name = "test_user"
    password = "test_password"
  }

  body = {
    type = "TextDataBody"
    data = "Sample request body"
  }
  
  headers = {
    "header_key_1" = "header_value_1",
    "header_key_2" = "header_value_2"
  }

  retry = {
    max-retries = 10
    status-codes-to-retry = [429]
  }
}
```
