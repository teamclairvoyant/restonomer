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

User can provide few attributes related to CSV parsing. Below are the list of such attributes:

| Parameter Name                |        Default Value        | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|:------------------------------|:---------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| column-name-of-corrupt-record |       _corrupt_record       | Allows renaming the new field having malformed string created by PERMISSIVE mode. <br/>This overrides `spark.sql.columnNameOfCorruptRecord`.                                                                                                                                                                                                                                                                                                                                                                                                                            |
| date-format                   |         yyyy-MM-dd          | Sets the string that indicates a date format.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| empty-value                   |      "" (empty string)      | Sets the string representation of an empty value.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| enforce-schema                |            true             | If it is set to true, the specified or inferred schema will be forcibly applied to datasource files, and headers in CSV files will be ignored. <br/>If the option is set to false, the schema will be validated against all headers in CSV files in the case when the header option is set to true. <br/>Field names in the schema and column names in CSV headers are checked by their positions taking into account spark.sql.caseSensitive. <br/>Though the default value is true, it is recommended to disable the enforceSchema option to avoid incorrect results. |
| escape                        |              \              | Sets a single character used for escaping quotes inside an already quoted value.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| header                        |            true             | Boolean flag to tell whether csv text contains header names or not.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| infer-schema                  |            true             | Infers the input schema automatically from data.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| ignore-leading-white-space    |            false            | A flag indicating whether or not leading whitespaces from values being read should be skipped.                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| ignore-trailing-white-space   |            false            | A flag indicating whether or not trailing whitespaces from values being read should be skipped.                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| line-sep                      |             \n              | Defines the line separator that should be used for parsing. Maximum length is 1 character.                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| locale                        |            en-US            | Sets a locale as language tag in IETF BCP 47 format. For instance, this is used while parsing dates and timestamps.                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| multi-line                    |            false            | Parse one record, which may span multiple lines, per file.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| nan-value                     |             NaN             | Sets the string representation of a non-number value.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| null-value                    |            null             | Sets the string representation of a null value.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| original-schema               |            None             | Defines the schema DDL for the dataframe.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| quote                         |              "              | Sets a single character used for escaping quoted values where the separator can be part of the value. <br/>For reading, if you would like to turn off quotations, you need to set not null but an empty string.                                                                                                                                                                                                                                                                                                                                                         |
| record-sep                    |             \n              | Delimiter by which rows are separated in a csv text.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| sep                           |              ,              | Delimiter by which fields in a row are separated in a csv text.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| timestamp-format              |     yyyy-MM-dd HH:mm:ss     | Sets the string that indicates a timestamp format.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| timestamp-ntz-format          | yyyy-MM-dd'T'HH:mm:ss[.SSS] | Sets the string that indicates a timestamp without timezone format.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
