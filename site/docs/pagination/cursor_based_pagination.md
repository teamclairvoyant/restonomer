# Cursor Based Pagination

Cursor-based pagination is a technique used in REST APIs to paginate through a large set of results. It provides a way to fetch a subset of data in a predictable and efficient manner.

In cursor-based pagination, instead of using traditional page numbers or offsets, the API client sends a cursor to the server. A cursor represents a specific position or marker within the dataset. The server uses this cursor to determine the subset of data to return to the client.

Apart from above details, user should be aware of the query param name for the next page.

Once we know above details, user can configure pagination in the below manner. Consider we are getting below response
from API:

```json
{
  "next": 222222,
  "data": [
    {
      "col_A": "val_1",
      "col_B": "val_2",
      "col_C": "val_3"
    }
  ]
}
```

In the above response, the next cursor is represented by `next`. This value will be used in the subsequent request in the query param like: `http://localhost:8080/cursor-based-pagination?cursor=222222`

Then, the pagination details can be captured in the checkpoint file in the below manner:

```hocon
    pagination = {
      type = "CursorBasedPagination"
      next-cursor-attribute = "$.next"
    }
```

The `next-cursor-attribute` is represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_cursor_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/cursor-based-pagination"
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
      type = "CursorBasedPagination"
      next-cursor-attribute = "$.next"
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
