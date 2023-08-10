# OAuth2 Authentication

The OAuth2 standard defines several grant types (or flows) to request and get an access token:

*   Authorization Code
*   Implicit
*   Client Credentials

Currently, Restonomer supports only `Client Credentials` grant type.

In order to use OAuth2 authentication in checkpoint, user need to provide below parameters:

| Parameter Name | Mandatory | Default Value | Description                        |
|:---------------|:---------:|:-------------:|:-----------------------------------|
| token-url      |    Yes    |       -       | URL to get the access token        |
| client-id      |    Yes    |       -       | The client ID for your app         |
| client-secret  |    Yes    |       -       | The client secret key for your app |
| scope          |    No     |     None      | A space-separated list of scopes   |

User can configure OAuth2 authentication in checkpoint file in below manner:

```hocon
authentication = {
  type = "OAuth2Authentication"
  grant-type = {
    name = "ClientCredentials"
    token-url = "http://localhost:8080/api/token"
    client-id = "test_client_id"
    client-secret = "test_client_secret"
    scope = "user-read-playback-state user-modify-playback-state"
  }
}
```
