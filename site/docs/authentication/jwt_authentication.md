# JWT Authentication

The JWT authentication in restonomer framework is represented by class `JWTAuthentication`.

User can configure JWT Authentication in the checkpoint file in the below format:

```hocon
authentication = {
  type = "JWTAuthentication"
  subject = "test-authentication"
  secret-key = "abcd1234"
}
```
