# API Key Authentication

The API key authentication in restonomer framework is represented by class `APIKeyAuthentication`.

The API key authentication config expects user to provide below 3 details:

* name of api key
* value of api key
* placeholder that denotes where to add the api key to the request (query param / header / cookies)

```hocon
authentication = {
  type = "APIKeyAuthentication"
  api-key-name = "test_api_key_name"
  api-key-value = "test_api_key_value"
  placeholder = "QueryParam"
}
```
