# Digest Authentication

The digest authentication in restonomer framework is represented by class `DigestAuthentication`.

For digest authentication, user needs to provide username and password for authentication:

```hocon
authentication = {
 type = "DigestAuthentication"
 user-name = "test_user"
 password = "test_password"
}
```
