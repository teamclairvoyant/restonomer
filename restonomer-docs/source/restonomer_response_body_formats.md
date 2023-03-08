The rest API can return the response datasets in various formats including json/csv/xml.
User need to provide required details to the restonomer about the specific body format type so that Restonomer can 
handle the datasets accordingly.

# JSON

The configuration about the response body type can be provided in the checkpoint file under `data.data-response.body`.

For example if we are getting below response from api:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678
}
```
The user needs to specify the body type as `json` in the below manner:

```hocon
name = "checkpoint_json_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/json-response-converter"
  }

  data-response = {
    body = {
      type = "JSON"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/converter"
    }
  }
}
```

In the response json body, if dataset is not present at the root level but in some other attribute of json, then that 
attribute can be mentioned via `data-column` attribute.

For example, if we get the below kind of json response where dataset is not at the root level but under `data.items`:

```json
{
  "data": {
    "items": [
      {
        "col_A": "val_1",
        "col_B": "val_2",
        "col_C": "val_3"
      }
    ]
  }
}
```

Then body can be configured in below manner:

```hocon
body = {
  type = "JSON"
  data-column = "data.items"
}
```

# CSV

The configuration about the response body type can be provided in the checkpoint file under `data.data-response.body`.

For example if we are getting below response from api:

```csv
col_A,col_B,col_C
5,4,3.4678
```

The user needs to specify the body type as `csv` in the below manner:

```hocon
name = "checkpoint_csv_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/csv-response-converter"
  }

  data-response = {
    body = {
      type = "csv"
    }

    persistence = {
      type = "FileSystem"
      file-format = "csv"
      file-path = "/tmp/converter"
    }
  }
}
```
