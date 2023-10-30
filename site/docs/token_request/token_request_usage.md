# How to use token response in the data request ?

If you want to get the `X` attribute from the token response, then you need to mention the value in the desired
field in the below format:

```text
token[X]
```

* In case of `ResponseBody`, the attribute `X` must be a dot notation of [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html) for the desired token.

  Suppose, we get the below token response body:

    ```json
    {
      "data": {
        "secret": "abcd1234"
      }
    }
    ```

  In the above case, in order to get the token value of 'secret', the dot notation is `$.data.secret`

* In case of `ResponseHeaders`, the attribute `X` must be a token header name.

Currently, user can use token value in below section of data request:

* authentication
* query-params
* headers

## Using token response in the query params section of data request

Suppose the response provided by token request is:

```json
{
  "data": {
    "bearer_token": "bearer_123"
  }
}
```

Now, if user wants to pass on the value of `bearer-token` to the `query-params` section of the data request, then user can configure the checkpoint file in below manner:

```hocon
name = "checkpoint_add_token_in_query_params"

token = {
  token-request = {
    url = "http://localhost:8080/token-for-query-params"
  }

  token-response-placeholder = "ResponseBody"
}

data = {
  data-request = {
    url = "http://localhost:8080/test-token-for-query-params"

    query-params = {
      "query_param_name_1" = "query_param_value_1",
      "query_param_name_2" = "token[$.data.bearer_token]"
    }
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/query_params"
    }
  }
}
```

## Using token response in the headers section of data request

Suppose the response provided by token request is:

```json
{
  "data": {
    "bearer_token": "bearer_123"
  }
}
```

Now, if user wants to pass on the value of `bearer-token` to the `headers` section of the data request, then user can configure the checkpoint file in below manner:

```hocon
name = "checkpoint_add_custom_headers_with_token"

token = {
  token-request = {
    url = "http://localhost:8080/token-for-headers"
  }

  token-response-placeholder = "ResponseBody"
}

data = {
  data-request = {
    url = "http://localhost:8080/custom-headers-with-token"

    headers = {
      "header_key_1" = "header_value_1",
      "header_key_2" = "token[$.data.bearer_token]"
    }
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/headers"
    }
  }
}
```
