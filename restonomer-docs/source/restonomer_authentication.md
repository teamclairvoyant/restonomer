The authentication in restonomer is represented by abstract class `RestonomerAuthentication`.

# Types of authentication 

The restonomer framework supports below authentication mechanisms:

* Basic Authentication
* Bearer Authentication
* API Key Authentication
* JWT Authentication
* Digest Authentication

## Basic Authentication

The basic authentication in restonomer framework is represented by class `BasicAuthentication`.

Basic authentication can be achieved in restonomer using 2 ways:

* By providing username and password

```hocon
authentication = {
 type = "BasicAuthentication"
 user-name = "test_user"
 password = "test_password"
}
```

* By providing basic auth token

```hocon
authentication = {
 type = "BasicAuthentication"
 basic-token = "abcd1234"
}
```

## Bearer Authentication

The bearer authentication in restonomer framework is represented by class `BearerAuthentication`.

User would need to provide just the bearer auth token to the `authentication` configuration in checkpoint:

```hocon
authentication = {
 type = "BearerAuthentication"
 bearer-token = "abcd1234"
}
```

## API Key Authentication

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

## JWT Authentication

The JWT authentication in restonomer framework is represented by class `JWTAuthentication`.

User can configure JWT Authentication in the checkpoint file in the below format:

```hocon
authentication = {
  type = "JWTAuthentication"
  subject = "test-authentication"
  secret-key = "abcd1234"
}
```

## Digest Authentication

The digest authentication in restonomer framework is represented by class `DigestAuthentication`.

For digest authentication, user needs to provide username and password for authentication:

```hocon
authentication = {
 type = "DigestAuthentication"
 user-name = "test_user"
 password = "test_password"
}
```
