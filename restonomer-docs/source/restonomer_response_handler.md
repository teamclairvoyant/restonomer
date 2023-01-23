Restonomer can handle various api response data format.
Below are some types along with a sample config.

# Json

The user needs to specify the data type as JSON in the body-format section in data-response.

```hocon
name = "checkpoint_json_handler"

data = {
  data-request = {
    url = "http://localhost:8080/add-column-transformation"
  }

  data-response = {
    body-format = "JSON"

    transformations = [
      {
        type = "add-literal-column"
        column-name = "col_D"
        column-value = "val_D"
        column-data-type = "string"
      }]
  }
}
```

# CSV

The user needs to specify the data type as CSV in the body-format section in data-response.

```hocon
name = "checkpoint_csv_handler"

data = {
  data-request = {
    url = "http://localhost:8080/add-column-transformation"
  }

  data-response = {
    body-format = "CSV"

    transformations = [
      {
        type = "add-literal-column"
        column-name = "col_D"
        column-value = "val_D"
        column-data-type = "string"
      }]
  }
}
```