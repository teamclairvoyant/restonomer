# Form Data Body

The FormData provides a way to construct a set of key/value pairs representing form fields and their values.
It uses the same format a form would use if the encoding type were set to "multipart/form-data".

A form data body can be set on a request in checkpoint file in the below manner:

```hocon
body = {
  type = "FormDataBody"
  data = {
    "k1" = "v1"
    "k2" = "v2"
  }
}
```

Below is the full example of checkpoint file:

```hocon
name = "checkpoint_form_data_request_body"

data = {
  data-request = {
    url = "http://localhost:8080/form-data-request-body"

    body = {
      type = "FormDataBody"
      data = {
        "k1" = "v1"
        "k2" = "v2"
      }
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
      file-path = "/tmp/body"
    }
  }
}
```
