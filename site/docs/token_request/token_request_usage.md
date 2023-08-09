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
