# Offset Based Pagination

Offset-based pagination is a common technique used in REST APIs to retrieve a large set of data in smaller, manageable chunks. It involves breaking down the results into pages and using an offset parameter to specify the starting point of each page.

Here's how offset-based pagination typically works:

1. The API consumer sends a request to the server, specifying the number of results to retrieve per page (often called "limit") and the offset (or starting point) for the first page.

2. The server processes the request and returns the requested number of results starting from the specified offset. For example, if the limit is set to 10 and the offset is 0, the server would return the first 10 results.

3. The API response includes the requested results as well as metadata, such as the total number of available results. This information helps the consumer understand how many pages exist and determine the next offset for subsequent requests.

4. To retrieve the next page of results, the consumer sends another request with an updated offset value. For example, if the offset for the first page was 0 and the limit was 10, the consumer might set the offset to 10 to retrieve the next set of 10 results.

This process continues until the consumer has retrieved all desired results or reaches the end of the data set. By adjusting the offset and limit parameters, the consumer can navigate through the result pages.

Apart from above details, user should be aware of the query param name for the next page.

Once we know above details, user can configure pagination in the below manner. Consider we are getting below response from API:

```json
{
    "offset": 0,
    "limit": 3,
    "count": 7,
    "data": [
      {
        "col_A": "val_A_1",
        "col_B": "val_B_1",
        "col_C": "val_C_1"
      },
      {
        "col_A": "val_A_2",
        "col_B": "val_B_2",
        "col_C": "val_C_2"
      },
      {
        "col_A": "val_A_3",
        "col_B": "val_B_3",
        "col_C": "val_C_3"
      }
    ]
}
```

In the above response, the offset attribute is represented by `offset`. This value will be used along with `limit` value to calculate next offset and then will be used in the subsequent request in the query param like: `http://localhost:8080/offset-based-pagination?offset=3`

Then, the pagination details can be captured in the checkpoint file in the below manner:

```hocon
pagination = {
  type = "OffsetBasedPagination"
  offset-attribute = "$.offset"
  limit-attribute = "$.limit"
  total-number-of-records-attribute = "$.count"
}
```

The `offset-attribute`, `limit-attribute` and `total-number-of-records-attribute` are represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_offset_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/offset-based-pagination"
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
      type = "OffsetBasedPagination"
      offset-attribute = "$.offset"
      limit-attribute = "$.limit"
      total-number-of-records-attribute = "$.count"
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
