# What is Restonomer Pagination ?

Restonomer provides support for pagination while fetching huge datasets from the REST API.
Some APIs are not able to provide the complete data in a single request, and hence they make use of pagination to load the data in consecutive pages. 
A separate http request gets created for each page.

Each API has its own custom pagination scheme, and Restonomer internally implements the solution to deal with the same.
The users just need to provide few configurations without letting themselves know about the internal details of the implementation.

# Types of Restonomer Pagination

## PageNumberWithTotalRecordsBasedPagination

In this pagination mechanism, the huge datasets are split into multiple pages.

The Rest API returns you a response with partial datasets in a single request and then again consecutive requests needs 
to be triggered in order to get the complete dataset.

Along with the dataset, the api provides other details that are required in order to fetch the next page, such as:

*   Total number of records in the complete dataset
*   Current page number

Apart from above details, user should be aware of the below details:

*   Limitation on maximum number of records that api return per page
*   The query param name for the next page

Once we know above details, user can configure pagination in the below manner. Consider we are getting below response 
from API:

```json
{
  "data": {
    "total": {
      "numberItems": 3
    },
    "page": 1,
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

In the above response: 

*   the total number of records in the complete dataset is represented by `data.total.numberItems`
*   the current page number is represented by `data.page`
*   assume that maximum number of records that api return per page is `1`
*   the query param name for next page is `page`

Then, the pagination details can be captured in the checkpoint file in the below manner:

```hocon
    pagination = {
      type = "PageNumberBasedPagination"
      total-number-of-records-attribute = "$.data.total.numberItems"
      current-page-number-attribute = "$.data.page"
      max-records-per-page = 1
      page-token-name = "page"
    }
```

The `total-number-of-records-attribute` and `current-page-number-attribute` are represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_page_number_with_total_records_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/page_number_with_total_records_based_pagination"
  }

  data-response = {
    body = {
      type = "JSON"
      data-column-name = "data.items"
    }

    pagination = {
      type = "PageNumberWithTotalRecordsBasedPagination"
      total-number-of-records-attribute = "$.data.total.numberItems"
      current-page-number-attribute = "$.data.page"
      max-records-per-page = 1
      page-token-name = "page"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/pagination"
    }
  }
}
```

## PageNumberWithTotalPagesBasedPagination

In this pagination mechanism, the huge datasets are split into multiple pages.

The Rest API returns you a response with partial datasets in a single request and then again consecutive requests needs to be triggered in order to get the complete dataset.

Along with the dataset, the api provides other details that are required in order to fetch the next page, such as:

*   Total number of pages in the complete response
*   Current page number

Apart from above details, user should be aware of the below details:

*   The query param name for the next page

Once we know above details, user can configure pagination in the below manner. Consider we are getting below response
from API:

```json
{
  "data": {
    "total": {
      "numberPages": 3
    },
    "page": 1,
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

In the above response:

*   the total number of pages in the complete dataset is represented by `data.total.numberPages`
*   the current page number is represented by `data.page`
*   the query param name for next page is `page`

Then, the pagination details can be captured in the checkpoint file in the below manner:

```hocon
    pagination = {
      type = "PageNumberWithTotalPagesBasedPagination"
      total-number-of-pages-attribute = "$.data.total.numberPages"
      current-page-number-attribute = "$.data.page"
      page-token-name = "page"
    }
```

The `total-number-of-pages-attribute` and `current-page-number-attribute` are represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_page_number_with_total_pages_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/page_number_with_total_pages_based_pagination"
  }

  data-response = {
    body = {
      type = "JSON"
      data-column-name = "data.items"
    }

    pagination = {
      type = "PageNumberWithTotalPagesBasedPagination"
      total-number-of-pages-attribute = "$.data.total.numberPages"
      current-page-number-attribute = "$.data.page"
      page-token-name = "page"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/pagination"
    }
  }
}
```

## CursorBasedPagination

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
      page-token-name = "cursor"
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
      type = "JSON"
      data-column-name = "data"
    }

    pagination = {
      type = "CursorBasedPagination"
      next-cursor-attribute = "$.next"
      page-token-name = "cursor"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/pagination"
    }
  }
}
```