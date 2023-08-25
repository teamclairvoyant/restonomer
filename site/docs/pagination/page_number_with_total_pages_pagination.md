# Page Number With Total Pages Based Pagination

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
      type = "Text"
      text-format = {
        type = "JSONTextFormat"
        data-column-name = "data.items"
      }
    }

    pagination = {
      type = "PageNumberWithTotalPagesBasedPagination"
      total-number-of-pages-attribute = "$.data.total.numberPages"
      current-page-number-attribute = "$.data.page"
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
