# CheckpointConfig

The checkpoint configuration is the entry point for the restonomer framework.
The restonomer framework expects you to provide checkpoint configurations to the restonomer context instance in order to
trigger a checkpoint.

The checkpoint configuration contains below config options to be provided by the user:

| Config Name | Mandatory | Default Value | Description                                                       |
|:------------|:---------:|:-------------:|:------------------------------------------------------------------|
| name        |    Yes    |       -       | Unique name for your checkpoint                                   |
| token       |    No     |       -       | Token request configuration represented by `TokenConfig` class    |
| data        |    Yes    |       -       | Main data request configuration represented by `DataConfig` class |

User can provide checkpoint configuration file in HOCON format in the below format:

```hocon
name = "sample_postman_checkpoint"

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

data = {
  data-request = {
    url = "https://postman-echo.com/basic-auth"

    authentication = {
      type = "BasicAuthentication"
      user-name = "postman"
      password = "token[$.secret]"
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
      file-path = "./rest-output/"
    }
  }
}
```
