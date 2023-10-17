# JSON

Restonomer can parse the api response of text type in JSON format. User need to configure the checkpoint in below format:

```hocon
name = "checkpoint_json_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/json-response-converter"
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
        primitives-as-string = true
      }
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/response_body"
    }
  }
}
```

Just like `primitives-as-string`, user can configure below other properties for JSON text format that will help restonomer for parsing:

| Parameter Name                         |        Default Value        | Description                                                                                                                                                  |
| :------------------------------------- | :-------------------------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| allow-backslash-escaping-any-character |            false            | Allows accepting quoting of all character using backslash quoting mechanism.                                                                                 |
| allow-comments                         |            false            | Ignores Java/C++ style comment in JSON records.                                                                                                              |
| allow-non-numeric-numbers              |            true             | Allows JSON parser to recognize set of “Not-a-Number” (NaN) tokens as legal floating number values.                                                          |
| allow-numeric-leading-zeros            |            false            | Allows leading zeros in numbers (e.g. 00012).                                                                                                                |
| allow-single-quotes                    |            true             | Allows single quotes in addition to double quotes.                                                                                                           |
| allow-unquoted-control-chars           |            false            | Allows JSON Strings to contain unquoted control characters <br/>(ASCII characters with a value less than 32, including tab and line feed characters) or not. |
| allow-unquoted-field-names             |            false            | Allows unquoted JSON field names.                                                                                                                            |
| column-name-of-corrupt-record          |       _corrupt_record       | Allows renaming the new field having malformed string created by PERMISSIVE mode. This overrides spark.sql.columnNameOfCorruptRecord.                        |
| data-column-name                       |            None             | The name of the column that actually contains the dataset. If present, the API will only parse the dataset of this column to the dataframe.                  |
| date-format                            |         yyyy-MM-dd          | Sets the string that indicates a date format.                                                                                                                |
| drop-field-if-all-null                 |            false            | Whether to ignore columns of all null values or empty arrays during schema inference.                                                                        |
| enable-date-time-parsing-fallback      |            true             | Allows falling back to the backward compatible (Spark 1.x and 2.0) behavior of parsing dates and timestamps <br/>if values do not match the set patterns.    |
| encoding                               |            UTF-8            | Decodes the JSON files by the given encoding type.                                                                                                           |
| infer-schema                           |            true             | Infers the input schema automatically from data.                                                                                                             |
| line-sep                               |             \n              | Defines the line separator that should be used for parsing. Maximum length is 1 character.                                                                   |
| locale                                 |            en-US            | Sets a locale as a language tag in IETF BCP 47 format. For instance, this is used while parsing dates and timestamps.                                        |
| mode                                   |          FAILFAST           | Allows a mode for dealing with corrupt records during parsing. Allowed values are PERMISSIVE, DROPMALFORMED, and FAILFAST.                                   |
| multi-line                             |            false            | Parse one record, which may span multiple lines, per file.                                                                                                   |
| prefers-decimal                        |            false            | Infers all floating-point values as a decimal type. If the values do not fit in decimal, then it infers them as doubles.                                     |
| primitives-as-string                   |            false            | Infers all primitive values as a string type.                                                                                                                |
| sampling-ratio                         |             1.0             | Defines the fraction of rows used for schema inferring.                                                                                                      |
| timestamp-format                       |     yyyy-MM-dd HH:mm:ss     | Sets the string that indicates a timestamp format.                                                                                                           |
| timestamp-ntz-format                   | yyyy-MM-dd'T'HH:mm:ss[.SSS] | Sets the string that indicates a timestamp without timezone format.                                                                                          |
| time-zone                              |             UTC             | Sets the string that indicates a time zone ID to be used to format timestamps in the JSON datasources or partition values.                                   |
