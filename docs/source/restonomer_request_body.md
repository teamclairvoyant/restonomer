# Request Body

## TextDataBody

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

## FormDataBody

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

## JSONDataBody

In its simplest form, the request’s body can be set as a json string.

A json body can be set on a request in checkpoint file in the below manner:

```hocon
body = {
  type = "JSONDataBody"
  data = """{"k1": "v1", "k2", "v2"}"""
}
```

Below is the full example of checkpoint file:

```hocon
name = "checkpoint_text_data_request_body"

data = {
  data-request = {
    url = "http://localhost:8080/text-data-request-body"

    body = {
      type = "JSONDataBody"
      data = """{"k1": "v1", "k2", "v2"}"""
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