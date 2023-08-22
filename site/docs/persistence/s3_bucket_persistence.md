# S3Bucket

The S3Bucket persistence allows user to persist the restonomer response dataframe to AWS S3 bucket.

User can persist dataframe to S3 buckets in below formats:

* CSV
* JSON
* XML
* Parquet

The S3Bucket persistence needs below arguments from the user:

| Input Arguments | Mandatory | Default Value | Description                                                                                                                                                                                                  |
| :-------------- | :-------: | :-----------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| bucket-name     |    Yes    |       -       | The name of the s3 bucket where the dataframe files need to be stored.                                                                                                                                       |
| file-format     |    Yes    |       -       | The format of the files to be persisted for the response dataframe represented by `FileFormat` class. <br/> Supported values are `CSVFileFormat`, `JSONFileFormat`, `XMLFileFormat` and `ParquetFileFormat`. |
| file-path       |    Yes    |       -       | The path of the directory where output files will be persisted.                                                                                                                                              |
| save-mode       |    No     | ErrorIfExists | This is used to specify the expected behavior of saving a DataFrame to a data source.<br/> Expected values are (append, overwrite, errorifexists, ignore).                                                   |

User can configure the S3Bucket persistence in the below manner:

```hocon
persistence = {
  type = "S3Bucket"
  bucket-name = "test-bucket"
  file-format = {
    type = "CSVFileFormat"
    header = false
  }
  file-path = "test-output-dir"
}
```

Apart from just mentioning the type of file format, user can also configure few attributes related to each file format.
The details of each attribute for all supported file formats are below:

## CSVFileFormat

User can provide below options to the `CSVFileFormat` instance:

| Parameter Name                |        Default Value        | Description                                                                                                                                                                        |
| :---------------------------- | :-------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| char-to-escape-quote-escaping |              \              | Sets a single character used for escaping the escape for the quote character.                                                                                                      |
| compression                   |            none             | Compression codec to use when saving to file. This can be one of the known case-insensitive shorten names (none, bzip2, gzip, lz4, snappy, and deflate).                           |
| date-format                   |         yyyy-MM-dd          | Sets the string that indicates a date format.                                                                                                                                      |
| empty-value                   |      "" (empty string)      | Sets the string representation of an empty value.                                                                                                                                  |
| encoding                      |            UTF-8            | Specifies encoding (charset) of saved CSV files.                                                                                                                                   |
| escape                        |              \              | Sets a single character used for escaping quotes inside an already quoted value.                                                                                                   |
| escape-quotes                 |            true             | A flag indicating whether values containing quotes should always be enclosed in quotes. Default is to escape all values containing a quote character.                              |
| header                        |            true             | Boolean flag to tell whether csv text contains header names or not.                                                                                                                |
| ignore-leading-white-space    |            false            | A flag indicating whether or not leading whitespaces from values being written should be skipped.                                                                                  |
| ignore-trailing-white-space   |            false            | A flag indicating whether or not trailing whitespaces from values being written should be skipped.                                                                                 |
| line-sep                      |             \n              | Defines the line separator that should be used for writing. Maximum length is 1 character.                                                                                         |
| null-value                    |            null             | Sets the string representation of a null value.                                                                                                                                    |
| quote                         |              "              | Sets a single character used for escaping quoted values where the separator can be part of the value. <br/>For writing, if an empty string is set, it uses u0000 (null character). |
| quote-all                     |            false            | A flag indicating whether all values should always be enclosed in quotes. Default is to only escape values containing a quote character.                                           |
| sep                           |              ,              | Delimiter by which fields in a row are separated in a csv text.                                                                                                                    |
| timestamp-format              |     yyyy-MM-dd HH:mm:ss     | Sets the string that indicates a timestamp format.                                                                                                                                 |
| timestamp-ntz-format          | yyyy-MM-dd'T'HH:mm:ss[.SSS] | Sets the string that indicates a timestamp without timezone format.                                                                                                                |



## JSONFileFormat

User can provide below options to the `JSONFileFormat` instance:

| Parameter Name       |        Default Value        | Description                                                                                                                                              |
| :------------------- | :-------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------------------- |
| compression          |            none             | Compression codec to use when saving to file. This can be one of the known case-insensitive shorten names (none, bzip2, gzip, lz4, snappy, and deflate). |
| date-format          |         yyyy-MM-dd          | Sets the string that indicates a date format.                                                                                                            |
| encoding             |            UTF-8            | Specifies encoding (charset) of saved CSV files.                                                                                                         |
| ignore-null-fields   |            false            | Whether to ignore null fields when generating JSON objects.                                                                                              |
| line-sep             |             \n              | Defines the line separator that should be used for writing. Maximum length is 1 character.                                                               |
| timestamp-format     |     yyyy-MM-dd HH:mm:ss     | Sets the string that indicates a timestamp format.                                                                                                       |
| timestamp-ntz-format | yyyy-MM-dd'T'HH:mm:ss[.SSS] | Sets the string that indicates a timestamp without timezone format.                                                                                      |
| timezone             |             UTC             | Sets the string that indicates a time zone ID to be used to format timestamps in the JSON datasources or partition values.                               |


## XMLFileFormat

User can provide below options to the `XMLFileFormat` instance:

| Parameter Name     |                  Default Value                  | Description                                                                                                                                                                                                                                                                                          |
| :----------------- | :---------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| array-element-name |                      item                       | Name of XML element that encloses each element of an array-valued column when writing.                                                                                                                                                                                                               |
| attribute-prefix   |                        _                        | The prefix for attributes so that we can differentiate attributes and elements. This will be the prefix for field names.                                                                                                                                                                             |
| compression        |                      None                       | Compression codec to use when saving to file. <br/>Should be the fully qualified name of a class implementing org.apache.hadoop.io.compress.CompressionCodec or one of case-insensitive shorten names (bzip2, gzip, lz4, and snappy). <br/>Defaults to no compression when a codec is not specified. |
| date-format        |                   yyyy-MM-dd                    | Controls the format used to write DateType format columns.                                                                                                                                                                                                                                           |
| declaration        | version="1.0" encoding="UTF-8" standalone="yes" | Content of XML declaration to write at the start of every output XML file, before the rootTag. Set to an empty string to suppress.                                                                                                                                                                   |
| null-value         |                      null                       | The value to write null value. Default is the string "null". When this is null, it does not write attributes and elements for fields.                                                                                                                                                                |
| root-tag           |                      rows                       | The root tag of your XML files to treat as the root. It can include basic attributes by specifying a value like "books foo="bar"".                                                                                                                                                                   |
| row-tag            |                       row                       | The row tag of your XML files to treat as a row.                                                                                                                                                                                                                                                     |
| timestamp-format   |        yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]         | Controls the format used to write TimestampType format columns.                                                                                                                                                                                                                                      |
| value-tag          |                     _VALUE                      | The tag used for the value when there are attributes in the element having no child.                                                                                                                                                                                                                 |


## ParquetFileFormat

User can provide below options to the `ParquetFileFormat` instance:

| Parameter Name       | Default Value | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| :------------------- | :-----------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| datetime-rebase-mode |   EXCEPTION   | The datetimeRebaseMode option allows specifying the rebasing mode for the values of the DATE, TIMESTAMP_MILLIS, TIMESTAMP_MICROS logical types from the Julian to Proleptic Gregorian calendar. <br/> Currently supported modes are: <br/> EXCEPTION: fails in reads of ancient dates/timestamps that are ambiguous between the two calendars. <br/> CORRECTED: loads dates/timestamps without rebasing. <br/> LEGACY: performs rebasing of ancient dates/timestamps from the Julian to Proleptic Gregorian calendar. |
| int96-rebase-mode    |   EXCEPTION   | The int96RebaseMode option allows specifying the rebasing mode for INT96 timestamps from the Julian to Proleptic Gregorian calendar. Currently supported modes are: <br/> EXCEPTION: fails in reads of ancient INT96 timestamps that are ambiguous between the two calendars. <br/> CORRECTED: loads INT96 timestamps without rebasing. <br/> LEGACY: performs rebasing of ancient timestamps from the Julian to Proleptic Gregorian calendar.                                                                        |
| merge-schema         |     false     | Sets whether we should merge schemas collected from all Parquet part-files.                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| compression          |    snappy     | Compression codec to use when saving to file. This can be one of the known case-insensitive shorten names (none, uncompressed, snappy, gzip, lzo, brotli, lz4, and zstd).                                                                                                                                                                                                                                                                                                                                             |


Now, in order to make this work, user need to first authenticate against AWS account.
This can be done in either of below 2 ways:

*   **Setting up environment variables**

    User need to set below two environment variables in their execution environment:

    *   `AWS_ACCESS_KEY` or `AWS_ACCESS_KEY_ID`
    *   `AWS_SECRET_KEY` or `AWS_SECRET_ACCESS_KEY`

    Users should know beforehand the values of above credentials for their AWS account.

*   **Setting up spark configs**

    User can add below spark configs in the `application.conf` file present under restonomer context directory.

    *   `spark.hadoop.fs.s3a.access.key`
    *   `spark.hadoop.fs.s3a.secret.key`

```hocon
    spark-configs = {
      "spark.hadoop.fs.s3a.access.key" = "<AWS_ACCESS_KEY>"
      "spark.hadoop.fs.s3a.secret.key" = "<AWS_SECRET_KEY>"
    }
```
