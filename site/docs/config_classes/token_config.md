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
