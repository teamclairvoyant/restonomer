# Text Data Body

In its simplest form, the request’s body can be set as a String.

A String/Text body can be set on a request in checkpoint file in the below manner:

```hocon
body = {
  type = "TextDataBody"
  data = "Sample request body"
}
```

Below is the full example of checkpoint file:

```hocon
name = "checkpoint_text_data_request_body"

data = {
  data-request = {
    url = "http://localhost:8080/text-data-request-body"

    body = {
      type = "TextDataBody"
      data = "Hello World"
    }
  }

  data-response = {
    body = {
      type = "JSON"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/body"
    }
  }
}
```