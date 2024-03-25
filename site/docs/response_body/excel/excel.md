# Excel

Restonomer can parse the api response of MS Excel file type. User need to configure the checkpoint in below
format:

```hocon
name = "checkpoint_excel_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/excel-response-converter"
  }

  data-response = {
    body = {
      type = "Excel"
      excel-format = {
        data-address = "'Transactions Report'!A2:G4"
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

## Excel Format Configurations

User can provide below options to the `excel-format` instance:

| Parameter Name                     |     Default Value     | Description                                                                                                                                                                                                                                                                                                                                                                                                                            |
|:-----------------------------------|:---------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| header                             |         true          | Boolean flag to tell whether given excel sheet contains header names or not.                                                                                                                                                                                                                                                                                                                                                           |
| data-address                       |          A1           | The location of the data to read from. Following address styles are supported: <br/> `B3:` Start cell of the data. Returns all rows below and all columns to the right. <br/> `B3:F35:` Cell range of data. Reading will return only rows and columns in the specified range. <br/> `'My Sheet'!B3:F35:` Same as above, but with a specific sheet. <br/> `MyTable[#All]:` Table of data. Returns all rows and columns in this table.   |
| treat-empty-values-as-nulls        |         true          | Treats empty values as null                                                                                                                                                                                                                                                                                                                                                                                                            |
| set-error-cells-to-fallback-values |         false         | If set false errors will be converted to null. If true, any ERROR cell values (e.g. #N/A) will be converted to the zero values of the column's data type.                                                                                                                                                                                                                                                                              |
| use-plain-number-format            |         false         | If true, format the cells without rounding and scientific notations                                                                                                                                                                                                                                                                                                                                                                    |
| infer-schema                       |         false         | Infers the input schema automatically from data.                                                                                                                                                                                                                                                                                                                                                                                       |
| add-color-columns                  |         false         | If it is set to true, adds field with coloured format                                                                                                                                                                                                                                                                                                                                                                                  |
| timestamp-format                   | "yyyy-mm-dd hh:mm:ss" | String timestamp format                                                                                                                                                                                                                                                                                                                                                                                                                |
| excerpt-size                       |          10           | If set and if schema inferred, number of rows to infer schema from                                                                                                                                                                                                                                                                                                                                                                     |
| max-rows-in-memory                 |         None          | If set, uses a streaming reader which can help with big files (will fail if used with xls format files)                                                                                                                                                                                                                                                                                                                                |
| max-byte-array-size                |         None          | See https://poi.apache.org/apidocs/5.0/org/apache/poi/util/IOUtils.html#setByteArrayMaxOverride-int-                                                                                                                                                                                                                                                                                                                                   |
| temp-file-threshold                |         None          | Number of bytes at which a zip entry is regarded as too large for holding in memory and the data is put in a temp file instead                                                                                                                                                                                                                                                                                                         |