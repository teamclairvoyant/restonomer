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

Apart from `data-column`, user can provide other attributes as well related to JSON parsing. Below are the list of
attributes:

| Parameter Name                |        Default Value        | Description                                                                                                                           |
|:------------------------------|:---------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------|
| column-name-of-corrupt-record |       _corrupt_record       | Allows renaming the new field having malformed string created by PERMISSIVE mode. This overrides spark.sql.columnNameOfCorruptRecord. |
| date-format                   |         yyyy-MM-dd          | Sets the string that indicates a date format.                                                                                         |
| infer-schema                  |            true             | Infers the input schema automatically from data.                                                                                      |
| line-sep                      |             \n              | Defines the line separator that should be used for parsing. Maximum length is 1 character.                                            |
| locale                        |            en-US            | Sets a locale as language tag in IETF BCP 47 format. For instance, this is used while parsing dates and timestamps.                   |
| multi-line                    |            false            | Parse one record, which may span multiple lines, per file.                                                                            |
| primitives-as-string          |            false            | Infers all primitive values as a string type.                                                                                         |
| timestamp-format              |     yyyy-MM-dd HH:mm:ss     | Sets the string that indicates a timestamp format.                                                                                    |
| timestamp-ntz-format          | yyyy-MM-dd'T'HH:mm:ss[.SSS] | Sets the string that indicates a timestamp without timezone format.                                                                   |
| original-schema               |            None             | Defines the schema DDL for the dataframe.                                                                                             |
