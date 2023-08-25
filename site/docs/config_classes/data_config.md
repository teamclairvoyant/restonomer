# DataConfig

The configurations related to the main data request and response are represented by `DataConfig` class.

User needs to provide below configs for data configuration:

| Config Name   | Mandatory | Default Value | Description                                                                                                                                            |
|:--------------|:---------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------|
| data-request  |    Yes    |       -       | The configuration for the main data request to be triggered. It follows the same structure as `RequestConfig` class.                                   |
| data-response |    Yes    |       -       | The configuration for defining the main data response like format, transformations, persistence, etc. It is represented by `DataResponseConfig` class. |

The data config is represented in checkpoint file in below format:

```hocon
data = {
  data-request = {
    url = "https://postman-echo.com/basic-auth"

    authentication = {
      type = "BasicAuthentication"
      user-name = "postman"
      password = "password"
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
