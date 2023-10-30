# HTML Table

Restonomer can parse the api response of text type in HTML table format. User need to configure the checkpoint in below format:

```hocon
name = "checkpoint_html_response_dataframe_converter"

data = {
  data-request = {
    url = "http://localhost:8080/html-response-converter"
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "HTMLTableTextFormat"
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

User can configure below other properties for HTML text format that will help restonomer for parsing:

| Parameter Name | Default Value | Mandatory | Description                                                                   |
| :------------- | :-----------: | :-------: | :---------------------------------------------------------------------------- |
| tableName      |     None      |    No     | The name of the table in the `table` tag that you want to read the data from. |
