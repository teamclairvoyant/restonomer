# Basic Authentication

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
