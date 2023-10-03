# Next Page URL Based Pagination

In this pagination mechanism, the api returns the link or URL to the next page for the subsequent dataset from API:

```json
{
  "next": "/v1/affiliates/2",
  "data": [
    {
      "col_A": "val_1",
      "col_B": "val_2",
      "col_C": "val_3"
    }
  ]
}
```

In the above response, the link to the next page is represented by `next`. This value will be used in the subsequent request like: `http://localhost:8080/v1/affiliates/2`

This pagination details can be captured in the checkpoint file in the below manner:

```hocon
pagination = {
  type = "NextPageURLBasedPagination"
  next-url-attribute = "$.next"
}
```

The `next-url-attribute` is represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_next_page_url_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/next-page-url-based-pagination"
  }

  data-response = {
    body = {
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
        data-column-name = "data"
      }
    }

    pagination = {
      type = "NextPageURLBasedPagination"
      next-url-attribute = "$.next"
    }

    persistence = {
      type = "LocalFileSystem"
      file-format = {
        type = "ParquetFileFormat"
      }
      file-path = "/tmp/pagination"
    }
  }
}
```
