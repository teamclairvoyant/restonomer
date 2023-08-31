# Bearer Authentication

The bearer authentication in restonomer framework is represented by class `BearerAuthentication`.

User would need to provide just the bearer auth token to the `authentication` configuration in checkpoint:

```hocon
authentication = {
 type = "BearerAuthentication"
 bearer-token = "abcd1234"
}
```
