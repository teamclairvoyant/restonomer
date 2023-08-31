# DataResponseConfig

In restonomer framework, the details about the http response (like response body format, transformations, persistence) are captured via `DataResponseConfig` class.

User need to provide below configs for Data Response Configuration:

| Config Name     | Mandatory | Default Value | Description                                                                                         |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------|
| body            |    Yes    |       -       | The body config represented by `DataResponseBodyConfig`                                             |
| transformations |    No     |       -       | List of transformations to be applied on the restonomer response dataframe                          |
| persistence     |    Yes    |       -       | The persistence attribute that tells where to persist the transformed restonomer response dataframe |

The response configurations are provided in the checkpoint file in the below manner:

```hocon
data-response = {
  body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
      }
    }

  transformations = [
    {
      type = "AddColumn"
      column-name = "col_D"
      column-value = "val_D"
      column-data-type = "string"
    }
  ]
  
  persistence = {
    type = "LocalFileSystem"
    file-format = {
      type = "ParquetFileFormat"
    }
    file-path = "./rest-output/"
  }
}
```
