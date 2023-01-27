Restonomer can handle various api response data format.
Below are some types along with a sample config.

# Json

The user needs to specify the data type as JSON in the body-format section in data-response.

```hocon
name = "checkpoint_json_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/json-response-converter"
  }

  data-response = {
    body-format = "JSON"

    persistence = {
      type = "file-system"
      file-format = "json"
      file-path = "/tmp/converter/json"
    }
  }
}
```

# CSV

The user needs to specify the data type as CSV in the body-format section in data-response.

```hocon
name = "checkpoint_csv_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/csv-response-converter"
  }

  data-response = {
    body-format = "CSV"

    persistence = {
      type = "file-system"
      file-format = "csv"
      file-path = "/tmp/converter/csv"
    }
  }
}
```
