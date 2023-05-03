# CheckpointConfig

The checkpoint configuration is the entry point for the restonomer framework.
The restonomer framework expects you to provide checkpoint configurations to the restonomer context instance in order to
trigger a checkpoint.

The checkpoint configuration contains below config options to be provided by the user:

| Config Name | Mandatory | Default Value | Description                                                       |
|:------------|:---------:|:-------------:|:------------------------------------------------------------------|
| name        |    Yes    |       -       | Unique name for your checkpoint                                   |
| token       |    No     |       -       | Token request configuration represented by `TokenConfig` class    |
| data        |    Yes    |       -       | Main data request configuration represented by `DataConfig` class |

User can provide checkpoint configuration file in HOCON format in the below format:

```hocon
name = "sample_postman_checkpoint"

token = {
  token-request = {
    url = "http://localhost:8080/token-response-body"

    authentication = {
      type = "BearerAuthentication"
      bearer-token = "test_token_123"
    }
  }

  token-response-placeholder = "ResponseBody"
}

data = {
  data-request = {
    url = "https://postman-echo.com/basic-auth"

    authentication = {
      type = "BasicAuthentication"
      user-name = "postman"
      password = "token[$.secret]"
    }
  }

  data-response = {
    body = {
      type = "JSON"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "./rest-output/"
    }
  }
}
```

# TokenConfig

The configurations related to the token request are represented by `TokenConfig` class:

User need to provide below configs for `TokenConfig`:

| Config Name                | Mandatory | Default Value | Description                                                                                                                              |
|:---------------------------|:---------:|:-------------:|:-----------------------------------------------------------------------------------------------------------------------------------------|
| token-request              |    Yes    |       -       | The configuration for the token request to be triggered. It follows the same structure as `RequestConfig` class.                         |
| token-response-placeholder |    Yes    |       -       | The place holder where the token response will have the credentials. <br/>It can contain 2 values: `ResponseBody` and `ResponseHeaders`. |

The token config are provided in the checkpoint file in the below manner:

```hocon
token = {
  token-request = {
    url = "http://localhost:8080/token-response-body"

    authentication = {
      type = "BearerAuthentication"
      bearer-token = "test_token_123"
    }
  }

  token-response-placeholder = "ResponseBody"
}
```

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

# RetryConfig

The configurations related to the auto retry mechanism are represented by `RetryConfig` class.

User need to provide below configs for Retry Configuration:

| Config Name           | Mandatory |              Default Value               | Description                                                                                                                      |
|:----------------------|:---------:|:----------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------|
| max-retries           |    No     |                    20                    | This config tells restonomer the maximum number of attempts to retry in case of a request failure                                |
| status-codes-to-retry |    No     | [403, 429, 502, 401, 500, 504, 503, 408] | This config provides the restonomer with the list of response status codes for which user wants restonomer to retry the request. |

The retry configurations are provided in the checkpoint file in the below manner:

```hocon
retry = {
  max-retries = 5
  status-codes-to-retry = [429, 301]
}
```

# DataConfig

The configurations related to the main data request and response are represented by `DataConfig` class.

User needs to provide below configs for data configuration:

| Config Name   | Mandatory | Default Value | Description                                                                                                                                            |
|:--------------|:---------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------|
| data-request  |    Yes    |       -       | The configuration for the main data request to be triggered. It follows the same structure as `RequestConfig` class.                                   |
| data-response |    Yes    |       -       | The configuration for defining the main data response like format, transformations, persistence, etc. It is represented by `DataResponseConfig` class. |

The data config is represented in checkpoint file in below format:

```hocon
data = {
  data-request = {
    url = "https://postman-echo.com/basic-auth"

    authentication = {
      type = "BasicAuthentication"
      user-name = "postman"
      password = "password"
    }
  }

  data-response = {
    body = {
      type = "JSON"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "./rest-output/"
    }
  }
}
```

# DataResponseConfig

In restonomer framework, the details about the http response (like response body format, transformations, persistence) are captured via `DataResponseConfig` class.

User need to provide below configs for Data Response Configuration:

| Config Name     | Mandatory | Default Value | Description                                                                                         |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------|
| body            |    Yes    |       -       | The body config represented by `DataResponseBodyConfig`                                             |
| transformations |    No     |       -       | List of transformations to be applied on the restonomer response dataframe                          |
| persistence     |    Yes    |       -       | The persistence attribute that tells where to persist the transformed restonomer response dataframe |

The response configurations are provided in the checkpoint file in the below manner:

```hocon
data-response = {
  body = {
    type = "JSON"
  }

  transformations = [
    {
      type = "AddColumn"
      column-name = "col_D"
      column-value = "val_D"
      column-data-type = "string"
    }
  ]
  
  persistence = {
    type = "FileSystem"
    file-format = "JSON"
    file-path = "./rest-output/"
  }
}
```

# DataResponseBodyConfig

In restonomer framework, the details about the http response body are captured via `DataResponseBodyConfig` class.

User need to provide below configs for Data Response Body Configuration:

| Config Name | Mandatory | Default Value | Description                                                                                                                               |
|:------------|:---------:|:-------------:|:------------------------------------------------------------------------------------------------------------------------------------------|
| type        |    Yes    |       -       | The response body format returned by the rest api. It can hold values like `json`, `csv` and `xml`.                                       |
| data-column |    No     |       -       | This is used in case the response is of json format and the dataset is not present at the root level but in some other attribute of json. |

The response body configurations are provided in the checkpoint file in the below manner:

```hocon
body = {
  type = "JSON"
  data-column = "data.items"
}
```
